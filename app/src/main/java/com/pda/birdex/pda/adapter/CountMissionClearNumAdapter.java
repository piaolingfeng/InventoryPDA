package com.pda.birdex.pda.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.entity.ContainerInfo;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chuming.zhuang on 2016/6/22.
 */
public class CountMissionClearNumAdapter extends RecyclerView.Adapter<CountMissionClearNumAdapter.CountMissionClearNumAdapterHolder> {

    Context mContext;
    OnRecycleViewItemClickListener onRecycleViewItemClickListener;
    List<ContainerInfo> list;

    public void setOnRecycleViewItemClickListener(OnRecycleViewItemClickListener onRecycleViewItemClickListener) {
        this.onRecycleViewItemClickListener = onRecycleViewItemClickListener;
    }

    public CountMissionClearNumAdapter(Context mContext,List<ContainerInfo> list){
        this.mContext = mContext;
        this.list = list;
    }

    public void setList(List<ContainerInfo> list) {
        this.list = list;
    }

    @Override
    public CountMissionClearNumAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CountMissionClearNumAdapterHolder(LayoutInflater.from(mContext).inflate(R.layout.item_countbussiness_layout,null));
    }

    @Override
    public void onBindViewHolder(CountMissionClearNumAdapterHolder holder, int position) {
        holder.position = position;
//        holder.tv_status.setVisibility(View.GONE);
//        holder.pll_item.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        holder.tv_clear_num.setText(list.get(position).getContainerId());
        holder.tv_last_time.setText(list.get(position).getCount()+"");
        holder.tv_status.setText(list.get(position).getArea());
        holder.tv_status.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        int size=0;
        if(list!=null)
            size=list.size();
        return size;
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
