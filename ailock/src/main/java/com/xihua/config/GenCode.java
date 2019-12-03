package com.xihua.config;

import com.xihua.bean.ColumnInfo;
import com.xihua.bean.TableInfo;
import com.xihua.service.IGenService;
import com.xihua.service.impl.GenServiceImpl;
import com.xihua.utils.GenUtils;
import com.xihua.utils.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GenCode {

    @Autowired
    private IGenService genService;

    public  void genCode(String tableName){
        TableInfo table = genService.selectTableByName(tableName);
        table.setModuleName("lock");
        List<ColumnInfo> columnList = genService.selectTableColumnsByName(tableName);
        // 表名转类名
        String className = GenUtils.tableToJava(tableName);
        // 基本路径
        String baseDir = Global.getBaseDir();
        String javaPath = "/src/main/java/";
        String resourcePath = "/src/main/resources/";
        String packagePath = "com/xihua/";
        // 声明路径
        String beanPath = baseDir + javaPath + packagePath + "/bean/" + className + ".java";
        String javaMapperPath = baseDir + javaPath + packagePath + "/mapper/" + className + "Mapper.java";
        String javaIServicePath = baseDir + javaPath + packagePath + "/service/I" + className + "Service.java";
        String javaServicePath = baseDir + javaPath + packagePath + "/service/impl/" + className + "ServiceImpl.java";
        String controllerPath = baseDir + javaPath + packagePath + "/controller/" + className + "Controller.java";
        String xmlPath = baseDir + resourcePath + "mapper/" + className + "Mapper.xml";
        // 生成代码
        genService.generatorCode(table, columnList, beanPath, "bean.java");
        genService.generatorCode(table, columnList, controllerPath, "Controller.java");
        genService.generatorCode(table, columnList, javaMapperPath, "Mapper.java");
        genService.generatorCode(table, columnList, javaIServicePath, "Service.java");
        genService.generatorCode(table, columnList, javaServicePath, "ServiceImpl.java");
        genService.generatorCode(table, columnList, xmlPath, "Mapper.xml");
    }


}
