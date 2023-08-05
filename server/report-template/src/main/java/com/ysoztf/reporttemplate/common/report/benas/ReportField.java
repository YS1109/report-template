package com.ysoztf.reporttemplate.common.report.benas;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReportField)) return false;

        ReportField that = (ReportField) o;

        if (!Objects.equals(fieldName, that.fieldName)) return false;
        return Objects.equals(displayName, that.displayName);
    }

    @Override
    public int hashCode() {
        int result = fieldName != null ? fieldName.hashCode() : 0;
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ReportField{" +
                "fieldName='" + fieldName + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
