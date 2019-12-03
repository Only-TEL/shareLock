package com.xihua.service;

import com.xihua.bean.ColumnInfo;
import com.xihua.bean.TableInfo;

import java.util.List;

public interface IGenService {

    /**
     * 查询数据库表信息
     *
     * @param tableInfo 表信息
     * @return 数据库表列表
     */
    public List<TableInfo> selectTableList(TableInfo tableInfo);

    /**
     * 根据表名查询
     * @param tableName
     * @return
     */
    TableInfo selectTableByName(String tableName);

    /**
     * 查询列
     * @param tableName
     * @return
     */
    public List<ColumnInfo> selectTableColumnsByName(String tableName);


    /**
     * 生成代码
     * @param table
     * @param columns
     * @param filePath
     * @param type
     */
    public void generatorCode(TableInfo table, List<ColumnInfo> columns, String filePath, String type);

}
