package com.goldemperor.PgdActivity;

import java.math.BigDecimal;

public class Log_ProcessOutPutEntry {

    private String outputdate;//汇报日期;

    private Integer fempid;//员工ID

    private String fprocessnumber;//工序编码

    private String fprocessname;//工序名称

    private String femp;//员工

    private BigDecimal fqty;//汇报数量

    public String getOutputdate() {
        return outputdate;
    }

    public void setOutputdate(String outputdate) {
        this.outputdate = outputdate;
    }

    public Integer getFempid() {
        return fempid;
    }

    public void setFempid(Integer fempid) {
        this.fempid = fempid;
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

    public String getFemp() {
        return femp;
    }

    public void setFemp(String femp) {
        this.femp = femp == null ? null : femp.trim();
    }


    public BigDecimal getFqty() {
        return fqty;
    }

    public void setFqty(BigDecimal fqty) {
        this.fqty = fqty;
    }

}