package com.hanix.myapplication.view.widget.wheel.adapter;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;

public interface WheelViewAdapter {

    int getItemsCount();

    View getItem(int index, View convertView, ViewGroup parent);

    View getEmptyItem(View convertView, ViewGroup parent);

    void registerDataSetObserver(DataSetObserver observer);

    void unregisterDataSetObserver(DataSetObserver observer);


}
