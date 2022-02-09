package com.junling.spring.aop.config;

public class SJLAopConfig {
    private String pointCut;
    private String aspectClass;
    private String aspectBefore;
    private String aspectAfter;
    private String aspectAfterthrow;
    private String aspectAfterThrowingName;

    public void setPointCut(String pointCut) {
        this.pointCut = pointCut;
    }

    public void setAspectClass(String aspectClass) {
        this.aspectClass = aspectClass;
    }

    public void setAspectBefore(String aspectBefore) {
        this.aspectBefore = aspectBefore;
    }

    public void setAspectAfter(String aspectAfter) {
        this.aspectAfter = aspectAfter;
    }

    public void setAspectAfterthrow(String aspectAfterthrow) {
        this.aspectAfterthrow = aspectAfterthrow;
    }

    public void setAspectAfterThrowingName(String aspectAfterThrowingName) {
        this.aspectAfterThrowingName = aspectAfterThrowingName;
    }

    public String getPointCut() {
        return pointCut;
    }

    public String getAspectClass() {
        return aspectClass;
    }

    public String getAspectBefore() {
        return aspectBefore;
    }

    public String getAspectAfter() {
        return aspectAfter;
    }

    public String getAspectAfterthrow() {
        return aspectAfterthrow;
    }

    public String getAspectAfterThrowingName() {
        return aspectAfterThrowingName;
    }
}
