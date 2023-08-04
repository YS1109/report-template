package com.ysoztf.reporttemplate.common.report.benas;

import java.util.List;

/**
 * 报表字段集合类
 * dimensionList：维度属性集合
 * indicatorList: 指标属性集合
 */
public class ReportFieldCollection {
    private List<ReportField> dimensionList;
    private List<ReportField> indicatorList;

    public List<ReportField> getDimensionList() {
        return dimensionList;
    }

    public void setDimensionList(List<ReportField> dimensionList) {
        this.dimensionList = dimensionList;
    }

    public List<ReportField> getIndicatorList() {
        return indicatorList;
    }

    public void setIndicatorList(List<ReportField> indicatorList) {
        this.indicatorList = indicatorList;
    }

    @Override
    public String toString() {
        return "ReportFieldCollection{" +
                "dimensionList=" + dimensionList +
                ", indicatorList=" + indicatorList +
                '}';
    }
}
