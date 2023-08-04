package com.ysoztf.reporttemplate.common.report.benas;

/**
 * 报表属性类
 * fieldName：属性字段名
 * displayName：报表展示名
 */
public class ReportField {
    private String fieldName;
    private String displayName;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return "ReportField{" +
                "fieldName='" + fieldName + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
