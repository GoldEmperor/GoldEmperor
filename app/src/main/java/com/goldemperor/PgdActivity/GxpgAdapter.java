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

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapDropDown;
import com.goldemperor.MainActivity.OnItemClickListener;
import com.goldemperor.MainActivity.define;
import com.goldemperor.R;
import com.goldemperor.Widget.CheckBox;
import com.goldemperor.Widget.ScrollListenerHorizontalScrollView;
import com.google.gson.Gson;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    public GxpgAdapter(List<ProcessWorkCardPlanEntry> ls, String[][] nameList, GxpgActivity gxpgActivity) {
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
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
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
        TextView tv_nodispatchingnumber;
        TextView tv_alreadynumber;
        TextView tv_nonumber;

        String[][] nameList;
        public ScrollListenerHorizontalScrollView ScrollView;


        public DefaultViewHolder(View itemView, final String[][] nameList) {
            super(itemView);
            //checkBox = (CheckBox) itemView.findViewById(R.id.check);
            nameDropDown = (BootstrapDropDown) itemView.findViewById(R.id.XLarge);
            edit_userNumber = (EditText) itemView.findViewById(R.id.edit_userNumber);
            edit_dispatchingnumber = (EditText) itemView.findViewById(R.id.edit_dispatchingnumber);

            ScrollView = (ScrollListenerHorizontalScrollView) itemView.findViewById(R.id.ScrollView);
            ScrollView.setOnScrollListener(this);

            tv_processcode = (TextView) itemView.findViewById(R.id.tv_processcode);
            tv_processname = (TextView) itemView.findViewById(R.id.tv_processname);
            tv_havedispatchingnumber = (TextView) itemView.findViewById(R.id.tv_havedispatchingnumber);
            tv_nodispatchingnumber = (TextView) itemView.findViewById(R.id.tv_nodispatchingnumber);
            tv_alreadynumber = (TextView) itemView.findViewById(R.id.tv_alreadynumber);
            tv_nonumber = (TextView) itemView.findViewById(R.id.tv_nonumber);

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
                    if (edit_userNumber.getText().toString().trim().length() >= 6&&edit_userNumber.isFocused()) {
                        getUserInfo(edit_userNumber.getText().toString().trim());
                    }
                }
            });
        }

        private void getUserInfo(String FNumber) {
            RequestParams params = new RequestParams(define.GetEmpByFNumber);
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

        public void setData(ProcessWorkCardPlanEntry p, final int position) {
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
            String Undispatchingnumber = String.valueOf((p.getUndispatchingnumber() == null ? p.getHavedispatchingnumber().intValue() : p.getUndispatchingnumber()).intValue());
            edit_dispatchingnumber.setText(String.valueOf(gxpgActivity.sc_ProcessWorkCardEntryList.get(position).getFqty().intValue()));

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
                    if(edit_dispatchingnumber.isFocused()&&edit_dispatchingnumber.getText().length()>0) {
                        gxpgActivity.sc_ProcessWorkCardEntryList.get(getAdapterPosition()).setFqty(new BigDecimal(edit_dispatchingnumber.getText().toString().trim()));
                        Log.e("jindi", "afterTextChanged:"+getAdapterPosition());
                    }
                }
            });

            tv_processcode.setText(p.getProcesscode());
            tv_processname.setText(p.getProcessname());

            //数据加载有延迟需要先判断
            if(gxpgActivity.sc_ProcessWorkCardEntryList.get(position)!=null) {
                nameDropDown.setText(gxpgActivity.sc_ProcessWorkCardEntryList.get(position).getName());
                edit_userNumber.setText(gxpgActivity.sc_ProcessWorkCardEntryList.get(position).getJobNumber());
            }

            tv_havedispatchingnumber.setText(String.valueOf(p.getHavedispatchingnumber().intValue()));

            tv_nodispatchingnumber.setText(Undispatchingnumber);
            tv_alreadynumber.setText(String.valueOf(p.getReportednumber().intValue()));
            tv_nonumber.setText(String.valueOf(p.getHavedispatchingnumber().intValue() - p.getReportednumber().intValue()));

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
