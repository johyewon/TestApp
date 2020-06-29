package com.hanix.myapplication.view.widget.wheel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.hanix.myapplication.R;
import com.hanix.myapplication.view.widget.wheel.adapter.NumericWheelAdapter;
import com.hanix.myapplication.view.widget.wheel.adapter.DayArrayAdapter;

import java.util.Calendar;
import java.util.Locale;

public class DateTimeWheel extends LinearLayout {

    private Calendar calendar = Calendar.getInstance(Locale.KOREA);

    private OnTimeChangedListener timeChangedListener = null;

    public DateTimeWheel(Context context) { this(context, null); }

    public DateTimeWheel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setOrientation(VERTICAL);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.date_time_wheel, this, true);

        final WheelView hours = (WheelView) findViewById(R.id.hour);
        NumericWheelAdapter hourAdapter = new NumericWheelAdapter(context, 0, 23, "%2d");
        hourAdapter.setItemResource(R.layout.wheel_text_item);
        hourAdapter.setItemTextResource(R.id.text);
        hours.setViewAdapter(hourAdapter);
        hours.setCyclic(true);

        final WheelView min = (WheelView) findViewById(R.id.mins);
        NumericWheelAdapter minAdapter = new NumericWheelAdapter(context, 0, 59, "%02d");
        minAdapter.setItemResource(R.layout.wheel_text_item);
        minAdapter.setItemTextResource(R.id.text);
        min.setViewAdapter(minAdapter);
        min.setCyclic(true);

        min.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                calendar.add(Calendar.MINUTE, newValue - oldValue);

                fireTimeChanged(calendar.getTimeInMillis());
            }
        });

        hours.setCurrentItem(calendar.get(Calendar.HOUR));
        min.setCurrentItem(calendar.get(Calendar.MINUTE));

        final WheelView day = (WheelView) findViewById(R.id.day);
        day.setViewAdapter(new DayArrayAdapter(context, calendar));
        day.setCyclic(true);
    }

    public DateTimeWheel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DateTimeWheel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void fireTimeChanged(long timeInMillis) {
        if(timeChangedListener != null)
            timeChangedListener.onTimeChanged(timeInMillis);
    }

    public void setOnTimeChangedListener(OnTimeChangedListener timeChangedListener) {
        this.timeChangedListener = timeChangedListener;
    }

    public interface OnTimeChangedListener {
        void onTimeChanged(long time);
    }
}
