package com.goldemperor.PgdActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nova on 2017/8/26.
 */

public class PgdResult {
    private String code;
    private String count;
    private ArrayList<WorkCardPlan> data;

    public void setCode(String code) {
        this.code = code;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setData(ArrayList<WorkCardPlan> data) {
        this.data = data;
    }

    public String getCode() {
        return this.code;
    }

    public String getCount() {
        return this.count;
    }

    public ArrayList<WorkCardPlan> getData() {
        return this.data;
    }

    public PgdResult() {
    }
}
