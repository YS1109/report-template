package com.ysoztf.reporttemplate.common.report;

import com.sun.istack.NotNull;
import com.ysoztf.reporttemplate.annotation.ReportEntityAnnotation;
import com.ysoztf.reporttemplate.common.report.benas.ReportField;
import com.ysoztf.reporttemplate.common.report.benas.ReportFieldCollection;
import com.ysoztf.reporttemplate.common.report.benas.ReportQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 生成报表工具类
 * 实现原理为：读取实体类上的注解，生成一个报表字段的集合，然后根据用户传入筛选条件使用 Jpa 的 Criteria API 进行动态Sql查询并返回结果
 */
@Component
public class ReportGenerateUtil {
    private final EntityManagerFactory entityManagerFactory;
    private static final String DIMENSION_ANNOTATION_TYPE = "dimension";
    private static final String INDICATOR_ANNOTATION_TYPE = "indicator";

    @Autowired
    public ReportGenerateUtil(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    private EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public ReportQueryResult generateReport(List<String> groupDimensions,
                                                    Class<?> clazz,
                                                    int pageNum,
                                                    int pageSize) {
        EntityManager localEntityManager = getEntityManager();
        try {
            // 获取报表实体类的所有维度和指标
            ReportFieldCollection reportFieldCollection = getReportFieldCollection(clazz);
            CriteriaBuilder criteriaBuilder = localEntityManager.getCriteriaBuilder();
            CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createTupleQuery();
            Root<?> root = criteriaQuery.from(clazz);
            // 构建group查询表达式
            List<Expression<?>> groupExpressions = createGroupExpressions(reportFieldCollection.getDimensionList(),
                    groupDimensions, root);
            // 构建select查询表达式
            List<Selection<?>> selectExpressions = createSelectExpressions(reportFieldCollection, groupDimensions,
                    criteriaBuilder, root);
            criteriaQuery.multiselect(selectExpressions);
            criteriaQuery.groupBy(groupExpressions);
            TypedQuery<Tuple> tupleTypedQuery = localEntityManager.createQuery(criteriaQuery);
            tupleTypedQuery.setFirstResult(pageNum * pageSize);
            tupleTypedQuery.setMaxResults(pageSize);
            List<Tuple> queryResult = tupleTypedQuery.getResultList();
            // 将查询结果替换成Map
            List<Map<String, Object>> resultItems = convertTupleListToMapList(queryResult);
            return buildReportQueryResult(resultItems,pageNum, pageSize, criteriaBuilder, root, groupExpressions,
                    localEntityManager);
        } finally {
            // 一定要保证entityManager被回收
            if (localEntityManager != null) {
                localEntityManager.close();
            }
        }
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

    /**
     * 构建group查询表达式
     * @param allDimensions
     * @param groupDimensions
     * @param root
     * @return
     */
    private List<Expression<?>> createGroupExpressions(List<ReportField> allDimensions,
                                                      List<String> groupDimensions,
                                                      @NotNull Root<?> root) {
        if (groupDimensions == null || groupDimensions.isEmpty() || allDimensions == null || allDimensions.isEmpty()) {
            return new ArrayList<>();
        }
        // 过滤掉无效的分组字段
        List<ReportField> validGroupDimensions = buildValidDimensions(allDimensions, groupDimensions);
        if (validGroupDimensions.isEmpty()) {
            return new ArrayList<>();
        }
        List<Expression<?>> groupExpressions = new ArrayList<>();
        for (ReportField dimension : validGroupDimensions) {
            groupExpressions.add(root.get(dimension.getFieldName()));
        }
        return groupExpressions;
    }

    /**
     * 过滤掉无效的分组字段
     * @param allDimensions
     * @param queryFields
     * @return
     */
    private List<ReportField> buildValidDimensions(List<ReportField> allDimensions, List<String> queryFields) {
        return allDimensions.stream()
                .filter(dimension -> queryFields.contains(dimension.getFieldName()))
                .collect(Collectors.toList());
    }

    /**
     * 构建select查询表达式
     * @param reportFieldCollection
     * @param groupDimensions
     * @param criteriaBuilder
     * @param root
     * @return
     */
    private List<Selection<?>> createSelectExpressions(ReportFieldCollection reportFieldCollection,
                                                       List<String> groupDimensions,
                                                       CriteriaBuilder criteriaBuilder,
                                                       @NotNull Root<?> root) {
        List<ReportField> allDimensions = reportFieldCollection.getDimensionList();
        List<ReportField> allIndicators = reportFieldCollection.getIndicatorList();
        List<Selection<?>> selectExpressions = new ArrayList<>();
        if (allDimensions != null && !allDimensions.isEmpty() && groupDimensions != null
                && !groupDimensions.isEmpty()) {
            List<ReportField> validDimensions = buildValidDimensions(allDimensions, groupDimensions);
            for (ReportField validDimension : validDimensions) {
                selectExpressions.add(root.get(validDimension.getFieldName()).alias(validDimension.getDisplayName()));
            }
        }
        if (allIndicators != null && !allIndicators.isEmpty()) {
            for (ReportField indicator : allIndicators) {
                selectExpressions.add(criteriaBuilder.sum(root.get(indicator.getFieldName()))
                        .alias(indicator.getDisplayName()));
            }
        }
        return  selectExpressions;
    }

    /**
     * 将查询结果的Tuple类型转换为Map类型
     * @param tupleList
     * @return
     */
    private List<Map<String, Object>> convertTupleListToMapList(List<Tuple> tupleList) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Tuple tuple : tupleList) {
            Map<String, Object> map = new HashMap<>();
            for (TupleElement<?> tupleElement : tuple.getElements()) {
                String fieldName = tupleElement.getAlias();
                Object fieldValue = tuple.get(tupleElement);
                map.put(fieldName, fieldValue);
            }
            result.add(map);
        }
        return result;
    }


    /**
     * 构建分页查询的结果
     * @param resultItems
     * @param pageNum
     * @param pageSize
     * @param criteriaBuilder
     * @param root
     * @param groupExpressions
     * @return
     */
    private ReportQueryResult buildReportQueryResult(List<Map<String, Object>> resultItems,
                                                     int pageNum,
                                                     int pageSize,
                                                     @NotNull CriteriaBuilder criteriaBuilder,
                                                     @NotNull Root<?> root,
                                                     List<Expression<?>> groupExpressions,
                                                     EntityManager localEntityManager) {
        ReportQueryResult reportQueryResult = new ReportQueryResult();
        reportQueryResult.setItems(resultItems);
        if (pageNum < 0) {
            pageNum = 0;
        }
        if (pageSize <= 0) {
            pageSize = 50;
        }
        reportQueryResult.setPageNum(pageNum);
        reportQueryResult.setPageSize(pageSize);
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<?> countRoot = countQuery.from(root.getJavaType());
        countQuery.select(criteriaBuilder.count(countRoot));
        if (groupExpressions != null && !groupExpressions.isEmpty()) {
            countQuery.groupBy(groupExpressions);
        }
        long totalItemsNum = localEntityManager.createQuery(countQuery).getSingleResult();
        long totalPageNum = (totalItemsNum + pageSize - 1) / pageSize;
        reportQueryResult.setTotalItemsNum(totalItemsNum);
        reportQueryResult.setTotalPageNum(totalPageNum);
        return reportQueryResult;
    }

}
