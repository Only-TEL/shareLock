package com.xihua.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class Global {

    private static final Logger log = LoggerFactory.getLogger(Global.class);
    /** 全局配置文件 */
    private static String NAME = "application.yml";
    private static Map<String, String> map = new HashMap();


    public static String getConfig(String key) {
        String value = (String)map.get(key);
        if (value == null) {
            Map yamlMap = null;

            try {
                yamlMap = YamlUtil.loadYaml(NAME);
                value = String.valueOf(YamlUtil.getProperty(yamlMap, key));
                map.put(key, value != null ? value : "");
            } catch (FileNotFoundException var4) {
                log.error("获取全局配置异常 {}", key);
            }
        }
        return value;
    }

    /**
     * 获取项目根目录
     * @return
     */
    public static String getBaseDir() {
        return (String)StringUtils.nvl(getConfig("gen.localdir"), "D:/ideaPro/shareLock");
    }

    /**
     * 获取系统的版本
     * @return
     */
    public static String getVersion(){
        return (String) StringUtils.nvl(getConfig("gen.version"), "1.0");
    }

    /**
     *
     * @return
     */
    public static String getName(){
        return (String)StringUtils.nvl(getConfig("gen.name"), "ailock");
    }

    /**
     * 获取代码生成路径的包名
     * @return
     */
    public static String getPackageName() {
        return (String)StringUtils.nvl(getConfig("gen.packageName"), "com.xihua");
    }

    /**
     * 获取代码生成的作者
     * @return
     */
    public static String getAuthor(){
        return (String)StringUtils.nvl(getConfig("gen.author"), "admin");
    }

    /**
     * 自动去除前缀,默认去除前缀
     * @return
     */
    public static String getAutoRemovePre(){
        return (String)StringUtils.nvl(getConfig("gen.autoRemovePre"), "false");
    }

    /**
     * 获取表的前缀
     * @return
     */
    public static String getTablePrefix() {
        return (String)StringUtils.nvl(getConfig("gen.tablePrefix"), "sys_");
    }

}
