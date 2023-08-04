package com.ysoztf.reporttemplate.common.report;

import com.sun.istack.NotNull;
import com.ysoztf.reporttemplate.annotation.ReportEntityAnnotation;
import com.ysoztf.reporttemplate.common.report.benas.ReportField;
import com.ysoztf.reporttemplate.common.report.benas.ReportFieldCollection;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * 生成报表工具类
 * 实现原理为：读取实体类上的注解，生成一个报表字段的集合，然后根据用户传入筛选条件使用 Jpa 的 Criteria API 进行动态Sql查询并返回结果
 */
@Component
public class ReportGenerateUtil {
    private final EntityManager entityManager;
    private static final String DIMENSION_ANNOTATION_TYPE = "dimension";
    private static final String INDICATOR_ANNOTATION_TYPE = "indicator";

    public ReportGenerateUtil(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Object generateReport(List<String> dimensions) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        // TODO: 2023/8/5 使用 Jpa 的 Criteria API进行查询
        return null;
    }

    /**
     * 使用反射根据注解生成实体类所拥有的所有报表字段集合
     * @param clazz
     * @return
     */
    public ReportFieldCollection getReportFieldCollection(Class<?> clazz) {
        ReportFieldCollection reportFieldCollection = new ReportFieldCollection();
        List<ReportField> dimensionList = new ArrayList<>();
        List<ReportField> indicatorList = new ArrayList<>();
        reportFieldCollection.setDimensionList(dimensionList);
        reportFieldCollection.setIndicatorList(indicatorList);
        for (Field field : clazz.getDeclaredFields()) {
            ReportEntityAnnotation reportEntityAnnotation = field.getAnnotation(ReportEntityAnnotation.class);
            if (reportEntityAnnotation == null) {
                continue;
            }
            String reportFieldType = reportEntityAnnotation.type();
            if (DIMENSION_ANNOTATION_TYPE.equals(reportFieldType)) {
                dimensionList.add(getReportFiled(reportEntityAnnotation, field));
            } else if (INDICATOR_ANNOTATION_TYPE.equals(reportFieldType)) {
                indicatorList.add(getReportFiled(reportEntityAnnotation, field));
            }
        }
        return reportFieldCollection;
    }

    /**
     * 根据注解组装一个报表属性
     * @param reportEntityAnnotation
     * @param field
     * @return
     */
    private ReportField getReportFiled(@NotNull ReportEntityAnnotation reportEntityAnnotation, Field field) {
        String fieldName = field.getName();
        String disPlayName = reportEntityAnnotation.displayName();
        if (!StringUtils.hasLength(disPlayName)) {
            disPlayName = fieldName;
        }
        ReportField reportField = new ReportField();
        reportField.setFieldName(fieldName);
        reportField.setDisplayName(disPlayName);
        return reportField;
    }

}
