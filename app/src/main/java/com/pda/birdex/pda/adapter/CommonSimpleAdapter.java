package com.pda.birdex.pda.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chuming.zhuang on 2016/3/31.
 */
public class CommonSimpleAdapter extends RecyclerView.Adapter<CommonSimpleAdapter.SimpleViewHolder> {

    List<String> list;
    Context mContext;

    OnRecycleViewItemClickListener onRecyclerViewItemClickListener;

    public void setOnRecyclerViewItemClickListener(OnRecycleViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public CommonSimpleAdapter(Context mContext, List<String> list) {
        this.mContext = mContext;
        if(list!=null){
            this.list = list;
        }else{
            this.list = new ArrayList<>();
        }
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SimpleViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_tablayout, null));
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        holder.position = position;
        holder.tv_context.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int position = 0;
        @Bind(R.id.tv_context)
        TextView tv_context;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (onRecyclerViewItemClickListener != null) {
                onRecyclerViewItemClickListener.onItemClick(position);
            }
        }
    }
    /*
    *修改数据源
    */
    public void setDataSource(List<String> list){
        if(list!=null){
            this.list=list;
            notifyDataSetChanged();
        }
    }
}
