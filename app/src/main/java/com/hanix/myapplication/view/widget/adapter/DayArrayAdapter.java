package com.hanix.myapplication.view.widget.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hanix.myapplication.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DayArrayAdapter extends AbstractWheelTextAdapter {

    private final int daysCount = 364;
    Calendar calendar;

    public DayArrayAdapter(Context context, Calendar calendar) {
        super(context, R.layout.time2_day, NO_RESOURCE);
        this.calendar = calendar;

        setItemTextResource(R.id.time2_monthday);
    }

    @Override
    public View getItem(int index, View cachedView, ViewGroup parent) {
        int day = -daysCount / 2 + index;
        Calendar newCalendar = (Calendar) calendar.clone();
        newCalendar.roll(Calendar.DAY_OF_YEAR, day);

        View view = super.getItem(index, cachedView, parent);

        TextView monthday = (TextView) view.findViewById(R.id.time2_monthday);
        DateFormat format = new SimpleDateFormat("M/dd");
        monthday.setText(format.format(newCalendar.getTime()));
        monthday.setText(0x00FFFFFF);

        return view;
    }

    @Override
    protected CharSequence getItemText(int index) {
        return "";
    }

    @Override
    public int getItemsCount() {
        return daysCount + 1;
    }
}
