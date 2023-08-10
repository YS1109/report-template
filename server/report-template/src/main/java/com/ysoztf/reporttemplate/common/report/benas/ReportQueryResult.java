package com.ysoztf.reporttemplate.common.report.benas;

import java.util.List;
import java.util.Map;

public class ReportQueryResult {
    private int pageNum;
    private int pageSize;
    private long totalPageNum;
    private long totalItemsNum;
    List<Map<String, Object>> items;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalPageNum() {
        return totalPageNum;
    }

    public void setTotalPageNum(long totalPageNum) {
        this.totalPageNum = totalPageNum;
    }

    public long getTotalItemsNum() {
        return totalItemsNum;
    }

    public void setTotalItemsNum(long totalItemsNum) {
        this.totalItemsNum = totalItemsNum;
    }

    public List<Map<String, Object>> getItems() {
        return items;
    }

    public void setItems(List<Map<String, Object>> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "ReportQueryResult{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", totalPageNum=" + totalPageNum +
                ", totalItemsNum=" + totalItemsNum +
                ", items=" + items +
                '}';
    }
}
