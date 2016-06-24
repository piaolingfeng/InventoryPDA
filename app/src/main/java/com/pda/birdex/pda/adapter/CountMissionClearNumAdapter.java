package com.pda.birdex.pda.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;
import com.zhy.android.percent.support.PercentLinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chuming.zhuang on 2016/6/22.
 */
public class CountMissionClearNumAdapter extends RecyclerView.Adapter<CountMissionClearNumAdapter.CountMissionClearNumAdapterHolder> {

    Context mContext;
    OnRecycleViewItemClickListener onRecycleViewItemClickListener;

    public void setOnRecycleViewItemClickListener(OnRecycleViewItemClickListener onRecycleViewItemClickListener) {
        this.onRecycleViewItemClickListener = onRecycleViewItemClickListener;
    }

    public CountMissionClearNumAdapter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public CountMissionClearNumAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CountMissionClearNumAdapterHolder(LayoutInflater.from(mContext).inflate(R.layout.item_countbussiness_layout,null));
    }

    @Override
    public void onBindViewHolder(CountMissionClearNumAdapterHolder holder, int position) {
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public class CountMissionClearNumAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @Bind(R.id.tv_clear_num)
        TextView tv_clear_num;
        @Bind(R.id.tv_status)
        TextView tv_status;
        @Bind(R.id.tv_last_time)
        TextView tv_last_time;
        @Bind(R.id.pll_item)
        PercentLinearLayout pll_item;
        int position=0;

        public CountMissionClearNumAdapterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(onRecycleViewItemClickListener!=null){
                onRecycleViewItemClickListener.onItemClick(position);
            }
        }
    }
}
