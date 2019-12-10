package com.xihua.service.impl;

import com.xihua.bean.ColumnInfo;
import com.xihua.bean.TableInfo;
import com.xihua.constants.Constants;
import com.xihua.mapper.GenMapper;
import com.xihua.service.IGenService;
import com.xihua.utils.*;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Service
public class GenServiceImpl implements IGenService {

    private static final Logger LOG = LoggerFactory.getLogger(GenServiceImpl.class);

    @Autowired
    private GenMapper genMapper;

    /**
     * 查询数据库表信息
     *
     * @param tableInfo 表信息
     * @return 数据库表列表
     */
    @Override
    public List<TableInfo> selectTableList(TableInfo tableInfo) {
        return genMapper.selectTableList(tableInfo);
    }

    @Override
    public TableInfo selectTableByName(String tableName) {
        TableInfo tableInfo = genMapper.selectTableByName(tableName);
        processTableInfo(tableInfo);
        return tableInfo;
    }

    @Override
    public List<ColumnInfo> selectTableColumnsByName(String tableName) {
        List<ColumnInfo> columnInfos = genMapper.selectTableColumnsByName(tableName);
        // 添加java属性
        List<ColumnInfo> list = GenUtils.transColums(columnInfos);
        return list;
    }

    public void processTableInfo(TableInfo table) {
        // 表名转换成Java属性名
        String className = CommonGenUtils.tableToJava(table.getTableName());
        table.setClassName(className);
        table.setClassname(StringUtils.uncapitalize(className));
        table.setPackageName("com.xihua");
        table.setAuthor(Global.getAuthor());
    }

    @Override
    public void generatorCode(TableInfo table, List<ColumnInfo> columns, String filePath, String type) {
        // 表名转换成Java属性名
        processTableInfo(table);
        // 列信息
        table.setColumns(CommonGenUtils.transColums(columns));
        // 设置主键
        table.setPrimaryKey(table.getColumnsLast());
        VelocityInitializer.initVelocity();
        VelocityContext context = GenUtils.getVelocityContext(table);
        // 获取模板列表
        List<String> templates = CommonGenUtils.getTemplates();
        PrintWriter pw = null;
        try {
            for (String template : templates) {
                if (template.contains(type)) {
                    // 渲染模板
                    pw = new PrintWriter(filePath);
                    Template tpl = Velocity.getTemplate(template, Constants.UTF8);
                    tpl.merge(context, pw);
                    // 输出
                    pw.flush();
                }

            }
        } catch (IOException e) {
            LOG.error("渲染模板失败，表名：" + table.getTableName(), e);
        } finally {
            if(pw != null){
                pw.close();
            }
        }
    }
}
