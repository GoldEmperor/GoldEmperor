/*
 * Copyright 2016 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.goldemperor.PgdActivity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapDropDown;
import com.beardedhen.androidbootstrap.api.defaults.ExpandDirection;
import com.goldemperor.MainActivity.OnItemClickListener;
import com.goldemperor.MainActivity.Utils;
import com.goldemperor.MainActivity.define;
import com.goldemperor.R;
import com.goldemperor.Widget.CheckBox;
import com.goldemperor.Widget.ScrollListenerHorizontalScrollView;
import com.google.gson.Gson;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.goldemperor.PgdActivity.PgdActivity.selectWorkCardPlan;

/**
 * Created by YOLANDA on 2016/7/22.
 */
public class GxpgAdapter extends SwipeMenuAdapter<GxpgAdapter.DefaultViewHolder> {

    private List<ProcessWorkCardPlanEntry> ls;

    private OnItemClickListener mOnItemClickListener;

    public static List<Integer> CheckBoxList;


    private String[][] nameList;

    private static String[] dropStrings;

    public static List<ScrollListenerHorizontalScrollView> ScrollViewList = new ArrayList<ScrollListenerHorizontalScrollView>();

    public static GxpgActivity gxpgActivity;

    public static DbManager dbManager;

    public static int listSize;

    public GxpgAdapter(List<ProcessWorkCardPlanEntry> ls, String[][] nameList, GxpgActivity gxpgActivity, DbManager dbManager) {
        this.ls = ls;
        CheckBoxList = new ArrayList<Integer>();
        for (int i = 0; i < 500; i++) {
            CheckBoxList.add(1);
        }
        this.nameList = nameList;
        dropStrings = new String[nameList.length];
        for (int i = 0; i < nameList.length; i++) {
            dropStrings[i] = nameList[i][1] + "  " + nameList[i][0];
        }
        this.gxpgActivity = gxpgActivity;
        this.dbManager = dbManager;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        listSize = ls.size();
        return ls == null ? 0 : ls.size();

    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_gpxg_item, parent, false);
    }

    @Override
    public GxpgAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {

        DefaultViewHolder viewHolder = new DefaultViewHolder(realContentView, nameList);
        viewHolder.mOnItemClickListener = mOnItemClickListener;
        ScrollViewList.add(viewHolder.ScrollView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GxpgAdapter.DefaultViewHolder holder, int position) {
        holder.setData(ls.get(position), position);
    }

    static class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ScrollListenerHorizontalScrollView.OnScrollListener {

        OnItemClickListener mOnItemClickListener;
        //CheckBox checkBox;
        BootstrapDropDown nameDropDown;
        EditText edit_userNumber;
        EditText edit_dispatchingnumber;


        TextView tv_processcode;
        TextView tv_processname;
        TextView tv_havedispatchingnumber;
        TextView tv_noReportednumber;

        String[][] nameList;
        public ScrollListenerHorizontalScrollView ScrollView;

        public DefaultViewHolder(View itemView, final String[][] nameList) {
            super(itemView);
            //checkBox = (CheckBox) itemView.findViewById(R.id.check);
            nameDropDown = (BootstrapDropDown) itemView.findViewById(R.id.XLarge);
            edit_userNumber = (EditText) itemView.findViewById(R.id.edit_userNumber);
            edit_dispatchingnumber = (EditText) itemView.findViewById(R.id.edit_dispatchingnumber);
            edit_dispatchingnumber.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            ScrollView = (ScrollListenerHorizontalScrollView) itemView.findViewById(R.id.ScrollView);
            ScrollView.setOnScrollListener(this);

            tv_processcode = (TextView) itemView.findViewById(R.id.tv_processcode);
            tv_processname = (TextView) itemView.findViewById(R.id.tv_processname);
            tv_havedispatchingnumber = (TextView) itemView.findViewById(R.id.tv_havedispatchingnumber);

            tv_noReportednumber = (TextView) itemView.findViewById(R.id.tv_noReportednumber);

            this.nameList = nameList;

            nameDropDown.setDropdownData(dropStrings);


            nameDropDown.setOnDropDownItemClickListener(new BootstrapDropDown.OnDropDownItemClickListener() {
                @Override
                public void onItemClick(ViewGroup parent, View v, int id) {
                    nameDropDown.setText(nameList[id][0]);
                    edit_userNumber.setText(nameList[id][1]);
                    gxpgActivity.sc_ProcessWorkCardEntryList.get(getAdapterPosition()).setName(nameList[id][0]);
                    gxpgActivity.sc_ProcessWorkCardEntryList.get(getAdapterPosition()).setJobNumber(nameList[id][1]);
                    gxpgActivity.sc_ProcessWorkCardEntryList.get(getAdapterPosition()).setFempid(Integer.valueOf(nameList[id][2]));
                }
            });

            edit_userNumber.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    //这个应该是在改变的时候会做的动作吧，具体还没用到过。
                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                              int arg3) {
                    // TODO Auto-generated method stub
                    //这是文本框改变之前会执行的动作
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub
                    /**这是文本框改变之后 会执行的动作
                     */
                    if (edit_userNumber.getText().toString().trim().length() >= 6 && edit_userNumber.isFocused()) {
                        getUserInfo(edit_userNumber.getText().toString().trim());
                    }
                }
            });
        }

        private void getUserInfo(String FNumber) {
            RequestParams params = new RequestParams(define.IP8341 + define.GetEmpByFNumber);
            params.addQueryStringParameter("FNumber", FNumber);
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(final String result) {
                    Gson g = new Gson();
                    UserResult ur = g.fromJson(result, UserResult.class);
                    if (!ur.getCount().equals("0")) {
                        nameDropDown.setText(ur.getData().getEmp_Name());
                        gxpgActivity.sc_ProcessWorkCardEntryList.get(getAdapterPosition()).setFempid(Integer.valueOf(ur.getData().getEmp_ID()));
                        gxpgActivity.sc_ProcessWorkCardEntryList.get(getAdapterPosition()).setName(ur.getData().getEmp_Name());
                        gxpgActivity.sc_ProcessWorkCardEntryList.get(getAdapterPosition()).setJobNumber(ur.getData().getEmp_Code());
                        gxpgActivity.sc_ProcessWorkCardEntryList.get(getAdapterPosition()).setFdeptmentid(ur.getData().getEmp_Departid());
                    } else {
                        nameDropDown.setText("无此工号");
                    }
                }

                //请求异常后的回调方法
                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.e("jindi", ex.toString());
                }

                //主动调用取消请求的回调方法
                @Override
                public void onCancelled(CancelledException cex) {
                }

                @Override
                public void onFinished() {
                }
            });
        }

        public void setData(final ProcessWorkCardPlanEntry p, final int position) {
            /*
            if (CheckBoxList.get(position) == 1) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);

            }

            checkBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
                @Override
                public void onChange(boolean checked) {
                    if (checked) {
                        CheckBoxList.set(getAdapterPosition(), 1);
                    } else {
                        CheckBoxList.set(getAdapterPosition(), 0);
                    }
                }
            });
     */
            if (getAdapterPosition() < gxpgActivity.sc_ProcessWorkCardEntryList.size()) {

                tv_noReportednumber.setText(String.valueOf((int) gxpgActivity.sc_ProcessWorkCardEntryList.get(getAdapterPosition()).getReportNumber()));

                edit_dispatchingnumber.setText(String.valueOf(gxpgActivity.sc_ProcessWorkCardEntryList.get(position).getFqty()));

                edit_dispatchingnumber.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                        // TODO Auto-generated method stub
                        //这个应该是在改变的时候会做的动作吧，具体还没用到过。
                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                                  int arg3) {
                        // TODO Auto-generated method stub
                        //这是文本框改变之前会执行的动作
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        // TODO Auto-generated method stub
                        /**这是文本框改变之后 会执行的动作
                         */
                        if (edit_dispatchingnumber.isFocused() && edit_dispatchingnumber.getText().length() > 0 && Utils.isNumeric(edit_dispatchingnumber.getText().toString())) {

                            if (Float.valueOf(edit_dispatchingnumber.getText().toString().trim()) != 0) {
                                //先不管超标设置总数
                                if (Float.valueOf(edit_dispatchingnumber.getText().toString().trim()) > p.getHavedispatchingnumber().floatValue()) {
                                    edit_dispatchingnumber.setText(String.valueOf(p.getHavedispatchingnumber().floatValue()));
                                }
                                gxpgActivity.sc_ProcessWorkCardEntryList.get(getAdapterPosition()).setFqty(new BigDecimal(edit_dispatchingnumber.getText().toString().trim()));
                                //设置后判断总数
                                float count = 0;
                                for (int i = 0; i < gxpgActivity.sc_ProcessWorkCardEntryList.size(); i++) {
                                    if (gxpgActivity.sc_ProcessWorkCardEntryList.get(i).getFprocessname().equals(gxpgActivity.sc_ProcessWorkCardEntryList.get(getAdapterPosition()).getFprocessname())) {
                                        count += gxpgActivity.sc_ProcessWorkCardEntryList.get(i).getFqty().floatValue();
                                        Log.e("jindi", "qty:" + gxpgActivity.sc_ProcessWorkCardEntryList.get(i).getFqty().floatValue());
                                    }

                                }

                                //超标就将数量设为0
                                if (count > p.getHavedispatchingnumber().floatValue()) {
                                    Log.e("jindi", "afterTextChanged:" + getAdapterPosition());
                                    edit_dispatchingnumber.setText("0");
                                    gxpgActivity.sc_ProcessWorkCardEntryList.get(getAdapterPosition()).setFqty(new BigDecimal(0));
                                    Toast.makeText(gxpgActivity, "无法派工,派工总数超标", Toast.LENGTH_LONG).show();
                                }


                                float per = gxpgActivity.sc_ProcessWorkCardEntryList.get(getAdapterPosition()).getFqty().floatValue() / (gxpgActivity.processWorkCardPlanEntryList.get(0).getHavedispatchingnumber().floatValue());

                                //Log.e("jindi","per:"+per);


                                try {
                                    GxpgPlan gxpgPlan = dbManager.selector(GxpgPlan.class).where("style", " = ", selectWorkCardPlan.getPlantbody()).and("processname", "=", gxpgActivity.processWorkCardPlanEntryList.get(getAdapterPosition()).getProcessname()).and("username", "=", gxpgActivity.sc_ProcessWorkCardEntryList.get(getAdapterPosition()).getName()).findFirst();
                                    if (gxpgPlan != null) {
                                        gxpgPlan.setPer(per);
                                        dbManager.saveOrUpdate(gxpgPlan);
                                    }
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                gxpgActivity.sc_ProcessWorkCardEntryList.get(getAdapterPosition()).setFqty(new BigDecimal(0));
                            }

                        }
                    }
                });

                tv_processcode.setText(p.getProcesscode());
                tv_processname.setText(p.getProcessname());

                //数据加载有延迟需要先判断
                if (gxpgActivity.sc_ProcessWorkCardEntryList.get(position) != null)

                {
                    nameDropDown.setText(gxpgActivity.sc_ProcessWorkCardEntryList.get(position).getName());
                    edit_userNumber.setText(gxpgActivity.sc_ProcessWorkCardEntryList.get(position).getJobNumber());
                }

                tv_havedispatchingnumber.setText(String.valueOf(p.getHavedispatchingnumber().floatValue()));

                try {
                    GxpgPlan gxpgPlan = dbManager.selector(GxpgPlan.class).where("style", " = ", p.getPlantbody()).and("processname", "=", p.getProcessname()).and("username", "=", gxpgActivity.sc_ProcessWorkCardEntryList.get(position).getName()).findFirst();

                    if (gxpgPlan != null) {
                        // tv_noReportednumber.setText(df.format(gxpgActivity.norecord*gxpgPlan.getPer()));
                        Log.e("jindi", "per:" + gxpgPlan.getPer());
                    }

                } catch (DbException e) {
                    e.printStackTrace();
                }

                tv_noReportednumber.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        //do something
                        final EditText editText = new EditText(gxpgActivity);
                        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                        AlertDialog.Builder inputDialog =
                                new AlertDialog.Builder(gxpgActivity);
                        inputDialog.setTitle("修改计工数").setView(editText);
                        inputDialog.setNegativeButton("取消", null);
                        inputDialog.setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        gxpgActivity.sc_ProcessWorkCardEntryList.get(position).setReportNumber(Float.valueOf(editText.getText().toString()));
                                        float count = 0;
                                        for (int i = 0; i < gxpgActivity.sc_ProcessWorkCardEntryList.size(); i++) {
                                            if (gxpgActivity.processWorkCardPlanEntryList.get(i).getProcessname().equals(gxpgActivity.processWorkCardPlanEntryList.get(position).getProcessname())) {
                                                count += gxpgActivity.sc_ProcessWorkCardEntryList.get(i).getReportNumber();
                                            }
                                        }
                                        if (count > gxpgActivity.norecord) {
                                            Toast.makeText(gxpgActivity, "计工数超过汇报数", Toast.LENGTH_LONG).show();
                                            gxpgActivity.sc_ProcessWorkCardEntryList.get(position).setReportNumber(0f);
                                        } else if (count < gxpgActivity.norecord) {
                                            Toast.makeText(gxpgActivity, "计工数少于汇报数", Toast.LENGTH_LONG).show();
                                        }
                                        gxpgActivity.mMenuAdapter.notifyDataSetChanged();

                                    }
                                }).show();
                    }
                });
                int alreadyNoreportedNumberCount = PgdActivity.selectWorkCardPlan.getAlreadynumberCount() - p.getReportednumber().intValue();
            }
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition());
            }

        }

        @Override
        public void onScroll(int scrollX) {
            for (int i = 0; i < ScrollViewList.size(); i++) {
                ScrollViewList.get(i).scrollTo(scrollX, ScrollView.getScrollY());
            }
            gxpgActivity.ScrollView.scrollTo(scrollX, ScrollView.getScrollY());

        }
    }

}
