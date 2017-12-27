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
package com.goldemperor.XJChenk;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goldemperor.MainActivity.OnItemClickListener;
import com.goldemperor.MainActivity.Utils;
import com.goldemperor.PzActivity.Sc_WorkCardAbnormity;
import com.goldemperor.PzActivity.YCListActivity;
import com.goldemperor.R;
import com.goldemperor.Widget.ScrollListenerHorizontalScrollView;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.goldemperor.PzActivity.YCListActivity.abnormityModel;

/**
 * Created by YOLANDA on 2016/7/22.
 */
public class XJListAdapter extends SwipeMenuAdapter<XJListAdapter.DefaultViewHolder> {

    private List<priceResult> ls;

    private OnItemClickListener mOnItemClickListener;



    public XJListAdapter(List<priceResult> ls) {
        this.ls = ls;

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
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_xjdlist_item, parent, false);
    }

    @Override
    public XJListAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {

        DefaultViewHolder viewHolder = new DefaultViewHolder(realContentView);
        viewHolder.mOnItemClickListener = mOnItemClickListener;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(XJListAdapter.DefaultViewHolder holder, int position) {
        holder.setData(ls.get(position));
    }


    static class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        OnItemClickListener mOnItemClickListener;
        RelativeLayout relativeLayout;

        private TextView tv_foperatorname;
        private TextView tv_fdate;
        private TextView tv_fnumber;

        private TextView tv_fitemname;
        private TextView tv_suppliername;
        private TextView tv_fneedauxqty;

        private TextView tv_fauxpricefor;
        private TextView tv_famountfor;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            relativeLayout= (RelativeLayout) itemView.findViewById(R.id.item);

            tv_foperatorname= (TextView) itemView.findViewById(R.id.tv_foperatorname);
            tv_fdate= (TextView) itemView.findViewById(R.id.tv_fdate);
            tv_fnumber= (TextView) itemView.findViewById(R.id.tv_fnumber);
            tv_fitemname= (TextView) itemView.findViewById(R.id.tv_fitemname);
            tv_suppliername= (TextView) itemView.findViewById(R.id.tv_suppliername);

            tv_fneedauxqty= (TextView) itemView.findViewById(R.id.tv_fneedauxqty);
            tv_fauxpricefor= (TextView) itemView.findViewById(R.id.tv_fauxpricefor);
            tv_famountfor= (TextView) itemView.findViewById(R.id.tv_famountfor);
        }

        public void setData(priceResult o) {
            relativeLayout.setOnClickListener(this);
            tv_foperatorname.setText("采购员:"+o.getFoperatorname());
            try {
                tv_fdate.setText("日期:"+ Utils.dateConvert(o.getFdate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tv_fnumber.setText("询价单号:"+o.getFnumber());
            tv_fitemname.setText("物料名称:"+o.getFitemname());
            tv_suppliername.setText("供应商:"+o.getSuppliername());
            tv_fneedauxqty.setText("数量:"+o.getFneedauxqty());
            tv_fauxpricefor.setText("单价:"+o.getFauxpricefor());
            tv_famountfor.setText("金额:"+o.getFsymbols()+o.getFamountfor());
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition());
            }

        }
    }


}
