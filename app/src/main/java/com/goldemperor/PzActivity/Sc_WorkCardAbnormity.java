package com.goldemperor.PzActivity;

import java.util.Date;

public class Sc_WorkCardAbnormity {
    private Integer finterID;

    private Integer fentryID;

    private Integer fexceptionID;

    private Integer fexceptionLevel;

    private String fnumber;

    private String fname;

    private String fdeptmentName;

    private String fdate;

    private Integer fempID;

    private Integer fdeptmentID;

    private Long fqty;

    private String fprocessing;

    private String fopinion;

    private String facceptedOpinion;

    private Integer fworkCardInterID;

    private Integer fworkCardEntryID;

    private String freCheck;

    public String getFreCheck() {
        return freCheck;
    }

    public void setFreCheck(String freCheck) {
        this.freCheck = freCheck;
    }

    public Integer getFinterID() {
        return finterID;
    }

    public void setFinterID(Integer FInterID) {
        this.finterID = finterID;
    }

    public Integer getFentryID() {
        return fentryID;
    }

    public void setFentryID(Integer fentryID) {
        this.fentryID = fentryID;
    }

    public Integer getFexceptionID() {
        return fexceptionID;
    }

    public void setFexceptionID(Integer fexceptionID) {
        this.fexceptionID = fexceptionID;
    }

    public Integer getFexceptionLevel() {
        return fexceptionLevel;
    }

    public void setFexceptionLevel(Integer fexceptionLevel) {
        this.fexceptionLevel = fexceptionLevel;
    }

    public String getFnumber() {
        return fnumber;
    }

    public void setFnumber(String Fnumber) {
        this.fnumber = fnumber == null ? null : fnumber.trim();
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getFdeptmentName() {
        return fdeptmentName;
    }

    public void setFdeptmentName(String fdeptmentName) {
        this.fdeptmentName = fdeptmentName;
    }

    public String getFdate() {
        return fdate;
    }

    public void setFdate(String fdate) {
        this.fdate = fdate;
    }

    public Integer getFempID() {
        return fempID;
    }

    public void setFempID(Integer fempID) {
        this.fempID = fempID;
    }

    public Integer getFdeptmentID() {
        return fdeptmentID;
    }

    public void setFdeptmentID(Integer fdeptmentID) {
        this.fdeptmentID = fdeptmentID;
    }

    public Long getFqty() {
        return fqty;
    }

    public void setFqty(Long fqty) {
        this.fqty = fqty;
    }

    public String getFprocessing() {
        return fprocessing;
    }

    public void setFprocessing(String fprocessing) {
        this.fprocessing = fprocessing == null ? null : fprocessing.trim();
    }

    public String getFopinion() {
        return fopinion;
    }

    public void setFopinion(String fopinion) {
        this.fopinion = fopinion == null ? null : fopinion.trim();
    }

    public String getFacceptedOpinion() {
        return facceptedOpinion;
    }

    public void setFacceptedOpinion(String facceptedOpinion) {
        this.facceptedOpinion = facceptedOpinion == null ? null : facceptedOpinion.trim();
    }

    public Integer getFworkCardInterID() {
        return fworkCardInterID;
    }

    public void setFworkCardInterID(Integer fworkCardInterID) {
        this.fworkCardInterID = fworkCardInterID;
    }

    public Integer getFworkCardEntryID() {
        return fworkCardEntryID;
    }

    public void setFworkCardEntryID(Integer fworkCardEntryID) {
        this.fworkCardEntryID = fworkCardEntryID;
    }
}