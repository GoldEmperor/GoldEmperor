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
package com.goldemperor.PzActivity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goldemperor.MainActivity.OnItemClickListener;
import com.goldemperor.R;
import com.goldemperor.Widget.ScrollListenerHorizontalScrollView;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.goldemperor.PzActivity.YCListActivity.selectAbnormity;


/**
 * Created by YOLANDA on 2016/7/22.
 */
public class ReasonAdapter extends SwipeMenuAdapter<ReasonAdapter.DefaultViewHolder> {

    private List<AbnormityModel> ls;

    private OnItemClickListener mOnItemClickListener;

    private static ReasonActivity reasonActivity;
    public ReasonAdapter(List<AbnormityModel> ls,ReasonActivity reasonActivity) {
        this.ls = ls;
        this.reasonActivity=reasonActivity;
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
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_reason_list, parent, false);
    }

    @Override
    public ReasonAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {

        DefaultViewHolder viewHolder = new DefaultViewHolder(realContentView);
        viewHolder.mOnItemClickListener = mOnItemClickListener;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReasonAdapter.DefaultViewHolder holder, int position) {
        holder.setData(ls.get(position),position);
    }


    static class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        OnItemClickListener mOnItemClickListener;


        private TextView tv_number;
        private TextView tv_reason;
        private CheckBox ckb_serious;
        private CheckBox ckb_slight;
        public DefaultViewHolder(View itemView) {
            super(itemView);
            tv_number = (TextView) itemView.findViewById(R.id.tv_number);
            tv_reason = (TextView) itemView.findViewById(R.id.tv_reason);
            ckb_serious= (CheckBox) itemView.findViewById(R.id.ckb_serious);
            ckb_slight= (CheckBox) itemView.findViewById(R.id.ckb_slight);
        }

        public void setData(AbnormityModel o, final int position) {
            tv_number.setText(position+1+",");
            tv_reason.setText(o.getFname());
            ckb_serious.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    // TODO Auto-generated method stub
                    if(isChecked){
                        ckb_slight.setChecked(false);
                        selectAbnormity.get(position)[2]="1";
                        selectAbnormity.get(position)[3]="已选";
                    }else{
                        if (!ckb_slight.isChecked()){
                            selectAbnormity.get(position)[3]="未选";
                        }
                    }

                    reasonActivity.freshReason();
                }
            });

            ckb_slight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    // TODO Auto-generated method stub
                    if(isChecked){
                        ckb_serious.setChecked(false);
                        selectAbnormity.get(position)[2]="0";
                        selectAbnormity.get(position)[3]="已选";
                    }else{
                        if (!ckb_serious.isChecked()){
                            selectAbnormity.get(position)[3]="未选";
                        }
                    }

                    reasonActivity.freshReason();
                }
            });

            if(selectAbnormity.get(position)[3].equals("已选")){
                if(selectAbnormity.get(position)[2].equals("0")){
                    ckb_slight.setChecked(true);
                }else{
                    ckb_serious.setChecked(true);
                }
            }else{
                ckb_slight.setChecked(false);
                ckb_serious.setChecked(false);
            }

        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition());
            }

        }

    }


}
