package com.hanix.myapplication.view.widget.adapter;

import android.content.Context;

public class ArrayWheelAdapter<T> extends AbstractWheelTextAdapter {

    private T items[];

    public ArrayWheelAdapter(Context context, T items[]) {
        super(context);

        this.items = items;
    }

    @Override
    protected CharSequence getItemText(int index) {
        if(index >= 0 && index < items.length) {
             T item = items[index];
            if(item instanceof CharSequence) {
                return (CharSequence) item;
            }
            return items.toString();
        }
        return null;
    }

    @Override
    public int getItemsCount() {
        return items.length;
    }
}
