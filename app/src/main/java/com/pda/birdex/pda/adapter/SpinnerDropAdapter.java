package com.pda.birdex.pda.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.entity.Merchant;

import java.util.List;

/**
 * Created by chuming.zhuang on 2016/6/30.
 */
public class SpinnerDropAdapter implements android.widget.SpinnerAdapter {
    List<Merchant> list;
    Context mContext;
    public SpinnerDropAdapter(List<Merchant> list,Context mContext){
        this.list = list;
        this.mContext = mContext;
    }
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_tablayout,null);
        TextView textView = (TextView) view.findViewById(R.id.tv_context);
        textView.setText(list.get(position).getMerchantName());
        return view;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        int size =0;
        if(list!=null)
            size = list.size();
        return size;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_tablayout,null);
        TextView textView = (TextView) view.findViewById(R.id.tv_context);
        textView.setText(list.get(position).getMerchantName());
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
