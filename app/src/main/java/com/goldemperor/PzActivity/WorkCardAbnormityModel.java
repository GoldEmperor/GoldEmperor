package com.goldemperor.PzActivity;

import java.util.Date;
import java.util.List;

public class WorkCardAbnormityModel {
    private Integer FInterID;

    private String FNumber;

    private String FDate;

    private Integer FEmpID;

    private Integer FDeptmentID;

    private Integer FExceptionID;

    private Long FQty;

    private String FProcessing;

    private String FOpinion;

    private String FAcceptedOpinion;

    private Integer FWorkCardInterID;

    private Integer FWorkCardEntryID;

    private List<WorkCardAbnormityEntryModel> Entry ;//异常分录信息

    public Integer getFInterID() {
        return FInterID;
    }

    public void setFInterID(Integer FInterID) {
        this.FInterID = FInterID;
    }

    public String getFNumber() {
        return FNumber;
    }

    public void setFNumber(String FNumber) {
        this.FNumber = FNumber == null ? null : FNumber.trim();
    }

    public String getFDate() {
        return FDate;
    }

    public void setFDate(String FDate) {
        this.FDate = FDate;
    }

    public Integer getFEmpID() {
        return FEmpID;
    }

    public void setFEmpID(Integer FEmpID) {
        this.FEmpID = FEmpID;
    }

    public Integer getFDeptmentID() {
        return FDeptmentID;
    }

    public void setFDeptmentID(Integer FDeptmentID) {
        this.FDeptmentID = FDeptmentID;
    }

    public Integer getFExceptionID() {
        return FExceptionID;
    }

    public void setFExceptionID(Integer FExceptionID) {
        this.FExceptionID = FExceptionID;
    }

    public Long getFQty() {
        return FQty;
    }

    public void setFQty(Long FQty) {
        this.FQty = FQty;
    }

    public String getFProcessing() {
        return FProcessing;
    }

    public void setFProcessing(String FProcessing) {
        this.FProcessing = FProcessing == null ? null : FProcessing.trim();
    }

    public String getFOpinion() {
        return FOpinion;
    }

    public void setFOpinion(String FOpinion) {
        this.FOpinion = FOpinion == null ? null : FOpinion.trim();
    }

    public String getFAcceptedOpinion() {
        return FAcceptedOpinion;
    }

    public void setFAcceptedOpinion(String FAcceptedOpinion) {
        this.FAcceptedOpinion = FAcceptedOpinion == null ? null : FAcceptedOpinion.trim();
    }

    public Integer getFWorkCardInterID() {
        return FWorkCardInterID;
    }

    public void setFWorkCardInterID(Integer FWorkCardInterID) {
        this.FWorkCardInterID = FWorkCardInterID;
    }

    public Integer getFWorkCardEntryID() {
        return FWorkCardEntryID;
    }

    public void setFWorkCardEntryID(Integer FWorkCardEntryID) {
        this.FWorkCardEntryID = FWorkCardEntryID;
    }

    public List<WorkCardAbnormityEntryModel> getEntry() {
        return Entry;
    }

    public void setEntry(List<WorkCardAbnormityEntryModel> Entry) {
        this.Entry = Entry;
    }
}