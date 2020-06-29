package com.hanix.myapplication.view.widget.wheel.adapter;

import android.content.Context;

public class NumericWheelAdapter extends AbstractWheelTextAdapter{

    public static final int DEFAULT_MAX_VALUE = 9;

    private static final int DEFAULT_MIN_VALUE = 0;

    private int minValue;
    private int maxValue;

    private String format;

    public NumericWheelAdapter(Context context) {
        this(context, DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE);
    }

    public NumericWheelAdapter(Context context, int minVal, int maxVal) {
        this(context, minVal, maxVal, null);
    }

    public NumericWheelAdapter(Context context, int minVal, int maxVal, String format) {
        super(context);

        this.minValue = minVal;
        this.maxValue = maxVal;
        this.format = format;
    }

    @Override
    protected CharSequence getItemText(int index) {
        if(index >=0 && index < getItemsCount()) {
            int value = minValue + index;

            if(0 == value) {
                return format != null ? String.format(format, value) : Integer.toString(value);
            } else {
                return Integer.toString(value);
            }
        }
        return null;
    }

    @Override
    public int getItemsCount() {
        return maxValue - minValue + 1;
    }
}
