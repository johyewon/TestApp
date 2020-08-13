package com.hanix.myapplication.view.widget.wheel.adapter;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractWheelAdapter implements WheelViewAdapter {

    private List<DataSetObserver> dataSetObservers;

    @Override
    public View getEmptyItem(View convertView, ViewGroup parent) { return null; }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        if(dataSetObservers == null)
            dataSetObservers = new LinkedList<>();

        dataSetObservers.add(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        if(dataSetObservers != null)
            dataSetObservers.remove(observer);
    }

    protected void notifyDataChangedEvent() {
        if (dataSetObservers != null) {
            for(DataSetObserver observer : dataSetObservers) {
                observer.onChanged();
            }
        }
    }

    protected void notifyDataInvalidatedEvent() {
        if(dataSetObservers != null) {
            for(DataSetObserver observer : dataSetObservers) {
                observer.onInvalidated();
            }
        }
    }
}
