package com.pda.birdex.pda.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hyj on 2016/6/27.
 */
public class BindNumAdapter extends RecyclerView.Adapter<BindNumAdapter.ViewHolder> {

    private Context context;
    private List<String> list;

    public BindNumAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    OnRecycleViewItemClickListener onRecyclerViewItemClickListener;

    public void setOnRecyclerViewItemClickListener(OnRecycleViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_bindnum, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.position = position;
        holder.tv_context.setText(list.get(position));
        holder.delete_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRecyclerViewItemClickListener != null) {
                    onRecyclerViewItemClickListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if(list!=null)
            size = list.size();
        return size;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        int position = 0;
        @Bind(R.id.tv_context)
        TextView tv_context;
        @Bind(R.id.delete_iv)
        ImageView delete_iv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
