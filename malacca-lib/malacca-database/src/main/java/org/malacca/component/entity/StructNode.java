package org.malacca.component.entity;//package com.sie.engine.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :yangxing 2021/7/15
 * </p>
 * <p>
 * Department :
 * </p>
 */
public class StructNode {

    private String toTableName;
    private String name;
    private String remark;
    private String comment;
    private String dataTypeCode;
    private String dataLength;
    private String dataPrecision;
    private String dataPattern;
    private String dataDefaultValue;
    private String isPrimaryKey;
    private String isNullable;

    private List<StructNode> children = new ArrayList<>();

    public enum DataTypeCode {
        NODE("node"), STRING("string"), INTEGER("integer"), INT("int"), NUMBER("number"), DATE("date"), CLOB("clob"), BLOB("blob"), TIMESTAMP("timestamp");

        String code;

        DataTypeCode(String code) {
            this.code = code;
        }

        public String code() {
            return code;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDataTypeCode() {
        return dataTypeCode;
    }

    public void setDataTypeCode(String dataTypeCode) {
        this.dataTypeCode = dataTypeCode;
    }

    public String getDataLength() {
        return dataLength;
    }

    public void setDataLength(String dataLength) {
        this.dataLength = dataLength;
    }

    public String getDataPrecision() {
        return dataPrecision;
    }

    public void setDataPrecision(String dataPrecision) {
        this.dataPrecision = dataPrecision;
    }

    public String getDataPattern() {
        return dataPattern;
    }

    public void setDataPattern(String dataPattern) {
        this.dataPattern = dataPattern;
    }


    public String getDataDefaultValue() {
        return dataDefaultValue;
    }

    public void setDataDefaultValue(String dataDefaultValue) {
        this.dataDefaultValue = dataDefaultValue;
    }


    public String getIsPrimaryKey() {
        return isPrimaryKey;
    }

    public void setIsPrimaryKey(String isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }

    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(String isNullable) {
        this.isNullable = isNullable;
    }


    public String getToTableName() {
        return toTableName;
    }

    public void setToTableName(String toTableName) {
        this.toTableName = toTableName;
    }

    public List<StructNode> getChildren() {
        return children;
    }

    public void setChildren(List<StructNode> children) {
        this.children = children;
    }
}
