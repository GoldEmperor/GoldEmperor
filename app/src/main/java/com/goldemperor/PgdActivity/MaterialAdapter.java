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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.goldemperor.MainActivity.OnItemClickListener;
import com.goldemperor.R;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.List;

/**
 * Created by YOLANDA on 2016/7/22.
 */
public class MaterialAdapter extends SwipeMenuAdapter<MaterialAdapter.DefaultViewHolder> {

    private List<Sc_WorkPlanMaterial> ls;

    private OnItemClickListener mOnItemClickListener;



    public MaterialAdapter(List<Sc_WorkPlanMaterial> ls) {
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
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_gxpg_material_list, parent, false);
    }

    @Override
    public MaterialAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {

        DefaultViewHolder viewHolder = new DefaultViewHolder(realContentView);
        viewHolder.mOnItemClickListener = mOnItemClickListener;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MaterialAdapter.DefaultViewHolder holder, int position) {
        holder.setData(ls.get(position));
    }

    static class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnItemClickListener mOnItemClickListener;
        private TextView tv_itemcode;
        private TextView tv_itemname;

        private TextView tv_fmodel;
        private TextView tv_fcolor;

        private TextView tv_itemuint;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            tv_itemcode = (TextView) itemView.findViewById(R.id.tv_itemcode);
            tv_itemname = (TextView) itemView.findViewById(R.id.tv_itemname);

            tv_fmodel = (TextView) itemView.findViewById(R.id.tv_fmodel);
            tv_fcolor = (TextView) itemView.findViewById(R.id.tv_fcolor);

            tv_itemuint = (TextView) itemView.findViewById(R.id.tv_itemuint);
        }

        public void setData(Sc_WorkPlanMaterial sc_workPlanMaterial) {
            tv_itemcode.setText(sc_workPlanMaterial.getItemcode());
            tv_itemname.setText(sc_workPlanMaterial.getItemname());

            tv_fmodel.setText(sc_workPlanMaterial.getFmodel());
            tv_fcolor.setText(sc_workPlanMaterial.getFcolor());

            tv_itemuint.setText(sc_workPlanMaterial.getItemuint());
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition());
            }

        }
    }

}
