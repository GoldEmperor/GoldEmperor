package com.goldemperor.PgdActivity;

import java.math.BigDecimal;
import java.util.Date;

public class Sc_WorkPlanMaterial {
    private Long fentryid;//分号

    private Long finterid;//主表ID

    private BigDecimal fpickedqty;//已领数量

    private BigDecimal fnopickedqty;//未领数量

    private BigDecimal frepickedqty;//补领数量


    private String planbill;//计划跟踪号

    private String itemcode;//子项物料编码


    private String itemname;//子项物料名称

    private String fmodel;//规格型号


    private String fcolor;//颜色

    private String itemuint;//子项单位

    private String fsize;//尺码
    

    private BigDecimal fuserate;//[使用比例(%)]

    private BigDecimal molecular;//分子

    private BigDecimal fdenominator;//分母

    private BigDecimal fneedqty;//需求数量

    private BigDecimal fmustqty;//应发数量


    public Long getFentryid() {
        return fentryid;
    }

    public void setFentryid(Long fentryid) {
        this.fentryid = fentryid;
    }

    public Long getFinterid() {
        return finterid;
    }

    public void setFinterid(Long finterid) {
        this.finterid = finterid;
    }

	public BigDecimal getFpickedqty() {
		return fpickedqty;
	}

	public void setFpickedqty(BigDecimal fpickedqty) {
		this.fpickedqty = fpickedqty;
	}

	public BigDecimal getFnopickedqty() {
		return fnopickedqty;
	}

	public void setFnopickedqty(BigDecimal fnopickedqty) {
		this.fnopickedqty = fnopickedqty;
	}

	public BigDecimal getFrepickedqty() {
		return frepickedqty;
	}

	public void setFrepickedqty(BigDecimal frepickedqty) {
		this.frepickedqty = frepickedqty;
	}

	public String getPlanbill() {
		return planbill;
	}

	public void setPlanbill(String planbill) {
		this.planbill = planbill;
	}

	public String getItemcode() {
		return itemcode;
	}

	public void setItemcode(String itemcode) {
		this.itemcode = itemcode;
	}


	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public String getFmodel() {
		return fmodel;
	}

	public void setFmodel(String fmodel) {
		this.fmodel = fmodel;
	}

	public String getFcolor() {
		return fcolor;
	}

	public void setFcolor(String fcolor) {
		this.fcolor = fcolor;
	}

	public String getItemuint() {
		return itemuint;
	}

	public void setItemuint(String itemuint) {
		this.itemuint = itemuint;
	}

	public String getFsize() {
		return fsize;
	}

	public void setFsize(String fsize) {
		this.fsize = fsize;
	}

	public BigDecimal getFuserate() {
		return fuserate;
	}

	public void setFuserate(BigDecimal fuserate) {
		this.fuserate = fuserate;
	}

	public BigDecimal getMolecular() {
		return molecular;
	}

	public void setMolecular(BigDecimal molecular) {
		this.molecular = molecular;
	}

	public BigDecimal getFdenominator() {
		return fdenominator;
	}

	public void setFdenominator(BigDecimal fdenominator) {
		this.fdenominator = fdenominator;
	}

	public BigDecimal getFneedqty() {
		return fneedqty;
	}

	public void setFneedqty(BigDecimal fneedqty) {
		this.fneedqty = fneedqty;
	}

	public BigDecimal getFmustqty() {
		return fmustqty;
	}

	public void setFmustqty(BigDecimal fmustqty) {
		this.fmustqty = fmustqty;
	}

  
}