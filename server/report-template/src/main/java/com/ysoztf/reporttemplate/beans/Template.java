package com.ysoztf.reporttemplate.beans;


import javax.persistence.*;

@Entity
@Table(name = "template")
public class Template {
    private int id;
    private String dimension1;
    private String dimension2;
    private String dimension3;
    private String dimension4;
    private String dimension5;
    private int indicator1;
    private int indicator2;
    private int indicator3;
    private int indicator4;
    private int indicator5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDimension1() {
        return dimension1;
    }

    public void setDimension1(String dimension1) {
        this.dimension1 = dimension1;
    }

    public String getDimension2() {
        return dimension2;
    }

    public void setDimension2(String dimension2) {
        this.dimension2 = dimension2;
    }

    public String getDimension3() {
        return dimension3;
    }

    public void setDimension3(String dimension3) {
        this.dimension3 = dimension3;
    }

    public String getDimension4() {
        return dimension4;
    }

    public void setDimension4(String dimension4) {
        this.dimension4 = dimension4;
    }

    public String getDimension5() {
        return dimension5;
    }

    public void setDimension5(String dimension5) {
        this.dimension5 = dimension5;
    }

    public int getIndicator1() {
        return indicator1;
    }

    public void setIndicator1(int indicator1) {
        this.indicator1 = indicator1;
    }

    public int getIndicator2() {
        return indicator2;
    }

    public void setIndicator2(int indicator2) {
        this.indicator2 = indicator2;
    }

    public int getIndicator3() {
        return indicator3;
    }

    public void setIndicator3(int indicator3) {
        this.indicator3 = indicator3;
    }

    public int getIndicator4() {
        return indicator4;
    }

    public void setIndicator4(int indicator4) {
        this.indicator4 = indicator4;
    }

    public int getIndicator5() {
        return indicator5;
    }

    public void setIndicator5(int indicator5) {
        this.indicator5 = indicator5;
    }

    @Override
    public String toString() {
        return "Template{" +
                "id=" + id +
                ", dimension1='" + dimension1 + '\'' +
                ", dimension2='" + dimension2 + '\'' +
                ", dimension3='" + dimension3 + '\'' +
                ", dimension4='" + dimension4 + '\'' +
                ", dimension5='" + dimension5 + '\'' +
                ", indicator1=" + indicator1 +
                ", indicator2=" + indicator2 +
                ", indicator3=" + indicator3 +
                ", indicator4=" + indicator4 +
                ", indicator5=" + indicator5 +
                '}';
    }
}
