package com.goldemperor.PgdActivity;

import java.math.BigDecimal;
import java.util.Date;


public class Sc_ProcessWorkCardEntry implements Cloneable {
    private String fmtono;

    private Integer fpartsid;

    private Integer fsrcicmointerid;

    private Integer fprdmoid;

    private Integer fmoid;//null

    private Integer foperplanningid;

    private Integer fproductid;

    private Integer fdeptmentid;

    private Integer fitemid;

    private Integer fprocessid;//null

    private String fprocessnumber;

    private String fprocessname;

    private String fsize;//null

    private Integer fempid;

    private String fmochinesid;//null

    private String fmochinecode;//null

    private BigDecimal fmustqty;

    private BigDecimal fqty;

    private Date fplanstartdate;

    private Date fplanfinishdate;

    private BigDecimal ffinishqty;

    private Date flastfinishdate;

    private BigDecimal fprice;

    private BigDecimal famount;

    private Integer fprocesserid;//0

    private Date fprocessdate;//null

    private String fcardno;

    private String fnotes;

    private String fversion;//0.1

    private Integer fsourceinterid;

    private Integer fsourceentryid;

    private Integer fprocessoutputid;

    private String fbatchno;

    private Integer fgroupprintno;

    private Integer fmodelid;

    private Integer froutingid;

    private Long finterid;

    private Integer fentryid;

    private String jobNumber;

    private String name;


    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getFinterid() {
        return finterid;
    }

    public void setFinterid(Long finterid) {
        this.finterid = finterid;
    }

    public Integer getFentryid() {
        return fentryid;
    }

    public void setFentryid(Integer fentryid) {
        this.fentryid = fentryid;
    }

    public String getFmtono() {
        return fmtono;
    }

    public void setFmtono(String fmtono) {
        this.fmtono = fmtono == null ? null : fmtono.trim();
    }

    public Integer getFpartsid() {
        return fpartsid;
    }

    public void setFpartsid(Integer fpartsid) {
        this.fpartsid = fpartsid;
    }

    public Integer getFsrcicmointerid() {
        return fsrcicmointerid;
    }

    public void setFsrcicmointerid(Integer fsrcicmointerid) {
        this.fsrcicmointerid = fsrcicmointerid;
    }

    public Integer getFprdmoid() {
        return fprdmoid;
    }

    public void setFprdmoid(Integer fprdmoid) {
        this.fprdmoid = fprdmoid;
    }

    public Integer getFmoid() {
        return fmoid;
    }

    public void setFmoid(Integer fmoid) {
        this.fmoid = fmoid;
    }

    public Integer getFoperplanningid() {
        return foperplanningid;
    }

    public void setFoperplanningid(Integer foperplanningid) {
        this.foperplanningid = foperplanningid;
    }

    public Integer getFproductid() {
        return fproductid;
    }

    public void setFproductid(Integer fproductid) {
        this.fproductid = fproductid;
    }

    public Integer getFdeptmentid() {
        return fdeptmentid;
    }

    public void setFdeptmentid(Integer fdeptmentid) {
        this.fdeptmentid = fdeptmentid;
    }

    public Integer getFitemid() {
        return fitemid;
    }

    public void setFitemid(Integer fitemid) {
        this.fitemid = fitemid;
    }

    public Integer getFprocessid() {
        return fprocessid;
    }

    public void setFprocessid(Integer fprocessid) {
        this.fprocessid = fprocessid;
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

    public String getFsize() {
        return fsize;
    }

    public void setFsize(String fsize) {
        this.fsize = fsize == null ? null : fsize.trim();
    }

    public Integer getFempid() {
        return fempid;
    }

    public void setFempid(Integer fempid) {
        this.fempid = fempid;
    }

    public String getFmochinesid() {
        return fmochinesid;
    }

    public void setFmochinesid(String fmochinesid) {
        this.fmochinesid = fmochinesid == null ? null : fmochinesid.trim();
    }

    public String getFmochinecode() {
        return fmochinecode;
    }

    public void setFmochinecode(String fmochinecode) {
        this.fmochinecode = fmochinecode == null ? null : fmochinecode.trim();
    }

    public BigDecimal getFmustqty() {
        return fmustqty;
    }

    public void setFmustqty(BigDecimal fmustqty) {
        this.fmustqty = fmustqty;
    }

    public BigDecimal getFqty() {
        return fqty;
    }

    public void setFqty(BigDecimal fqty) {
        this.fqty = fqty;
    }

    public Date getFplanstartdate() {
        return fplanstartdate;
    }

    public void setFplanstartdate(Date fplanstartdate) {
        this.fplanstartdate = fplanstartdate;
    }

    public Date getFplanfinishdate() {
        return fplanfinishdate;
    }

    public void setFplanfinishdate(Date fplanfinishdate) {
        this.fplanfinishdate = fplanfinishdate;
    }

    public BigDecimal getFfinishqty() {
        return ffinishqty;
    }

    public void setFfinishqty(BigDecimal ffinishqty) {
        this.ffinishqty = ffinishqty;
    }

    public Date getFlastfinishdate() {
        return flastfinishdate;
    }

    public void setFlastfinishdate(Date flastfinishdate) {
        this.flastfinishdate = flastfinishdate;
    }

    public BigDecimal getFprice() {
        return fprice;
    }

    public void setFprice(BigDecimal fprice) {
        this.fprice = fprice;
    }

    public BigDecimal getFamount() {
        return famount;
    }

    public void setFamount(BigDecimal famount) {
        this.famount = famount;
    }

    public Integer getFprocesserid() {
        return fprocesserid;
    }

    public void setFprocesserid(Integer fprocesserid) {
        this.fprocesserid = fprocesserid;
    }

    public Date getFprocessdate() {
        return fprocessdate;
    }

    public void setFprocessdate(Date fprocessdate) {
        this.fprocessdate = fprocessdate;
    }

    public String getFcardno() {
        return fcardno;
    }

    public void setFcardno(String fcardno) {
        this.fcardno = fcardno == null ? null : fcardno.trim();
    }

    public String getFnotes() {
        return fnotes;
    }

    public void setFnotes(String fnotes) {
        this.fnotes = fnotes == null ? null : fnotes.trim();
    }

    public String getFversion() {
        return fversion;
    }

    public void setFversion(String fversion) {
        this.fversion = fversion == null ? null : fversion.trim();
    }

    public Integer getFsourceinterid() {
        return fsourceinterid;
    }

    public void setFsourceinterid(Integer fsourceinterid) {
        this.fsourceinterid = fsourceinterid;
    }

    public Integer getFsourceentryid() {
        return fsourceentryid;
    }

    public void setFsourceentryid(Integer fsourceentryid) {
        this.fsourceentryid = fsourceentryid;
    }

    public Integer getFprocessoutputid() {
        return fprocessoutputid;
    }

    public void setFprocessoutputid(Integer fprocessoutputid) {
        this.fprocessoutputid = fprocessoutputid;
    }

    public String getFbatchno() {
        return fbatchno;
    }

    public void setFbatchno(String fbatchno) {
        this.fbatchno = fbatchno == null ? null : fbatchno.trim();
    }

    public Integer getFgroupprintno() {
        return fgroupprintno;
    }

    public void setFgroupprintno(Integer fgroupprintno) {
        this.fgroupprintno = fgroupprintno;
    }

    public Integer getFmodelid() {
        return fmodelid;
    }

    public void setFmodelid(Integer fmodelid) {
        this.fmodelid = fmodelid;
    }

    public Integer getFroutingid() {
        return froutingid;
    }

    public void setFroutingid(Integer froutingid) {
        this.froutingid = froutingid;
    }

    public Object clone() {
        Sc_ProcessWorkCardEntry o = null;
        try {
            o = (Sc_ProcessWorkCardEntry) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }
}