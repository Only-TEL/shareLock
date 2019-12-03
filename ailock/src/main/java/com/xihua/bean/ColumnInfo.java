package com.xihua.bean;

public class ColumnInfo {

    /**
     * 字段名称
     */
    private String columnName;

    /**
     * 字段类型
     */
    private String dataType;

    /**
     * 列描述
     */
    private String columnComment;
    /**
     * 是否可以为空
     * YES NO
     */
    private String isNullable;
    /**
     * 字符类型的长度
     */
    private String characterMaximumLength;
    /**
     * 最大位数
     */
    private String numericPrecision;
    /**
     * 小数位数
     */
    private String numericScale;
    /**
     * 列的索引：PRI > UNI > MUL > ''
     * 主键   唯一 非空   没有索引或者是一个非唯一的复合索引的非前导列
     */
    private String columnKey;
    /**
     * 附加项属性，比如：auto_increment
     */
    private String extra;
    /**
     * 列在表中的索引位置，从1开始
     */
    private Integer ordinalPosition;

    /**
     * Java属性类型
     */
    private String attrType;

    /**
     * Java属性名称(第一个字母大写)，如：user_name => UserName
     */
    private String attrName;

    /**
     * Java属性名称(第一个字母小写)，如：user_name => userName
     */
    private String attrname;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) throws Exception {
        this.columnComment = columnComment;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getAttrname() {
        return attrname;
    }

    public void setAttrname(String attrname) {
        this.attrname = attrname;
    }

    public String getAttrType() {
        return attrType;
    }

    public void setAttrType(String attrType) {
        this.attrType = attrType;
    }

    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(String isNullable) {
        this.isNullable = isNullable;
    }

    public String getCharacterMaximumLength() {
        return characterMaximumLength;
    }

    public void setCharacterMaximumLength(String characterMaximumLength) {
        this.characterMaximumLength = characterMaximumLength;
    }

    public String getNumericPrecision() {
        return numericPrecision;
    }

    public void setNumericPrecision(String numericPrecision) {
        this.numericPrecision = numericPrecision;
    }

    public String getNumericScale() {
        return numericScale;
    }

    public void setNumericScale(String numericScale) {
        this.numericScale = numericScale;
    }

    public String getColumnKey() {
        return columnKey;
    }

    public void setColumnKey(String columnKey) {
        this.columnKey = columnKey;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public Integer getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(Integer ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    @Override
    public String toString() {
        return "ColumnInfo{" +
                "columnName='" + columnName + '\'' +
                ", dataType='" + dataType + '\'' +
                ", columnComment='" + columnComment + '\'' +
                ", isNullable='" + isNullable + '\'' +
                ", characterMaximumLength='" + characterMaximumLength + '\'' +
                ", numericPrecision='" + numericPrecision + '\'' +
                ", numericScale='" + numericScale + '\'' +
                ", columnKey='" + columnKey + '\'' +
                ", extra='" + extra + '\'' +
                ", ordinalPosition=" + ordinalPosition +
                ", attrType='" + attrType + '\'' +
                ", attrName='" + attrName + '\'' +
                ", attrname='" + attrname + '\'' +
                '}';

    }
}