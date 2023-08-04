package com.ysoztf.reporttemplate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动生成报表字段注解
 * type：取值有 dimension 和 indicator。dimension代表该字段类型为维度，indicator代表该字段类型为指标
 * displayName：生成报表的展示名，可由用户自定义。如果不传该字段，后续逻辑会自动取属性名
 * 例如：
 *   @ReportEntityAnnotation(type = "indicator", displayName = "customIndicator1") 代表该字段类型为维度，报表属性展示名为
 *   customIndicator1
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReportEntityAnnotation {
    String type() default "";
    String displayName() default "";
}
