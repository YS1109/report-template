package com.ysoztf.reporttemplate;

import com.ysoztf.reporttemplate.beans.Template;
import com.ysoztf.reporttemplate.common.report.ReportGenerateUtil;
import com.ysoztf.reporttemplate.common.report.benas.ReportQueryResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@SpringBootTest
class ReportTemplateApplicationTests {
    @Autowired
    private ReportGenerateUtil reportGenerateUtil;

    @Test
    public void generateReportFieldsTest() {
        System.out.println(reportGenerateUtil.getReportFieldCollection(Template.class));
    }

    @Test
    public void generateReportTest() {
        List<String> dimensions = new ArrayList<>();
        dimensions.add("dimension1");
        dimensions.add("dimension2");
        ReportQueryResult result = reportGenerateUtil.generateReport(dimensions, Template.class, 0, 10);
        System.out.println(result);
    }

}
