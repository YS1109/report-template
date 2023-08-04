package com.ysoztf.reporttemplate;

import com.ysoztf.reporttemplate.beans.Template;
import com.ysoztf.reporttemplate.common.report.ReportGenerateUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class ReportTemplateApplicationTests {
    @Autowired
    private ReportGenerateUtil reportGenerateUtil;

    @Test
    public void generateReportFieldsTest() {
        System.out.println(reportGenerateUtil.getReportFieldCollection(Template.class));
    }

}
