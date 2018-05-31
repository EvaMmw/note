package com.example.android.newnote;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Derrick on 2018/5/30.
 */

public class MySpinnerAdapter implements SpinnerAdapter {


    private Context mContext;
    private CharSequence[] mStrings;

    public MySpinnerAdapter(Context context,CharSequence[] strings){
        this.mContext = context;
        this.mStrings = strings;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return mStrings.length;
    }

    @Override
    public Object getItem(int position) {
        return mStrings[position].toString();
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
        TextView textView = new TextView(mContext);
        textView.setTextSize(20);
        textView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        textView.setText(mStrings[position]);
        return textView;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView textView = new TextView(mContext);
        textView.setTextSize(20);
        textView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        textView.setText(mStrings[position]);
        return textView;
    }

}
