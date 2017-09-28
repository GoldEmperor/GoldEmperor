package com.goldemperor.PgdActivity;

import java.math.BigDecimal;

public class RouteEntry {
    private Integer fentryid;//分录ID

    private Integer finterid;

    private Integer fpartsid;

    private String partname;//部件

    private Integer fitemid;

    private String materialcode;//物料编码

    private String materialname;//物料名称

    private String fprocessnumber;//工序编码

    private String fprocessname;//工序名称

    private BigDecimal fprice;//工价

    private String fnote;//备注

    public Integer getFentryid() {
        return fentryid;
    }

    public void setFentryid(Integer fentryid) {
        this.fentryid = fentryid;
    }

    public Integer getFinterid() {
        return finterid;
    }

    public void setFinterid(Integer finterid) {
        this.finterid = finterid;
    }

    public Integer getFpartsid() {
        return fpartsid;
    }

    public void setFpartsid(Integer fpartsid) {
        this.fpartsid = fpartsid;
    }

    public String getPartname() {
        return partname;
    }

    public void setPartname(String partname) {
        this.partname = partname == null ? null : partname.trim();
    }

    public Integer getFitemid() {
        return fitemid;
    }

    public void setFitemid(Integer fitemid) {
        this.fitemid = fitemid;
    }

    public String getMaterialcode() {
        return materialcode;
    }

    public void setMaterialcode(String materialcode) {
        this.materialcode = materialcode == null ? null : materialcode.trim();
    }

    public String getMaterialname() {
        return materialname;
    }

    public void setMaterialname(String materialname) {
        this.materialname = materialname == null ? null : materialname.trim();
    }

    public String getFprocessnumber() {
        return fprocessnumber;
    }

    public void setFprocessnumber(String fprocessnumber) {
        this.fprocessnumber = fprocessnumber == null ? null : fprocessnumber.trim();
    }

    public String getFprocessname() {
        return fprocessname;
    }

    public void setFprocessname(String fprocessname) {
        this.fprocessname = fprocessname == null ? null : fprocessname.trim();
    }

    public BigDecimal getFprice() {
        return fprice;
    }

    public void setFprice(BigDecimal fprice) {
        this.fprice = fprice;
    }

    public String getFnote() {
        return fnote;
    }

    public void setFnote(String fnote) {
        this.fnote = fnote == null ? null : fnote.trim();
    }
}