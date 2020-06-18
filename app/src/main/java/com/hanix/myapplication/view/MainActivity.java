package com.hanix.myapplication.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.hanix.myapplication.R;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelAdapter;
import kankan.wheel.widget.adapters.WheelViewAdapter;

// https://github.com/KimoInfo/android-wheel/blob/3efd0fd18a71261c818286f8157814bd1b142086/wheel-demo/res/layout/slot_machine_layout.xml
public class MainActivity extends AppCompatActivity {

    private boolean wheelScrolled = false;

    static WheelView mainSlot1, mainSlot2, mainSlot3;
    static String slot1Result, slot2Result, slot3Result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainSlot1 = findViewById(R.id.mainSlot1);
        mainSlot2 = findViewById(R.id.mainSlot2);
        mainSlot2 = findViewById(R.id.mainSlot3);

        initWheel(mainSlot1);
        initWheel(mainSlot2);
        initWheel(mainSlot3);

        mixWheel(mainSlot1);
        mixWheel(mainSlot2);
        mixWheel(mainSlot3);

    }

    private void initWheel(WheelView wheelView) {
        wheelView.setViewAdapter(new SlotMachineAdapter(this));
        wheelView.setCurrentItem((int) (Math.random() * 10));
        wheelView.setBackgroundColor(Color.TRANSPARENT);
        wheelView.setCyclic(true);
        wheelView.setForegroundGravity(Gravity.CENTER);
        wheelView.setEnabled(false);
        wheelView.setClickable(false);
        wheelView.setOutlineAmbientShadowColor(Color.TRANSPARENT);
        wheelView.setVisibleItems(3);
        wheelView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        wheelView.addChangingListener(changedListener);
        wheelView.addScrollingListener(scrollListener);
    }

    private WheelView getWheel(int id) {
        return (WheelView) findViewById(id);
    }

    OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {
            wheelScrolled = true;
        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            wheelScrolled = false;
            int id = wheel.getId();
            String result = String.valueOf(wheel.getCurrentItem());
            updateStatus(id, result);
        }
    };

    private OnWheelChangedListener changedListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            if(!wheelScrolled) {
                int id = wheel.getId();
                String result = String.valueOf(wheel.getCurrentItem());
                updateStatus(id, result);
            }
        }
    };

    private void updateStatus(int id, String result) {
        if(id == R.id.mainSlot1) {
            slot1Result = result;
        } else if(id == R.id.mainSlot2) {
            slot2Result = result;
        } else if(id == R.id.mainSlot3) {
            slot3Result = result;
        }
    }

    public void mixWheel(WheelView wheelView) {
        wheelView.scroll(-350 + (int) (Math.random() * 50), 1000);
    }



    /*
    Slot Machine Adapter
     */
    private class SlotMachineAdapter extends AbstractWheelAdapter {

        final int IMAGE_WIDTH = 60;
        final int IMAGE_HEIGHT = 36;

        private final int items[] = new int[] {
                android.R.drawable.star_big_on,
                android.R.drawable.stat_sys_warning,
                android.R.drawable.radiobutton_on_background,
                android.R.drawable.ic_delete
        };

        private List<SoftReference<Bitmap>> images;
        final ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(IMAGE_WIDTH, IMAGE_HEIGHT);

        private Context context;

        public SlotMachineAdapter(Context context) {
            this.context = context;
            images = new ArrayList<SoftReference<Bitmap>>(items.length);
            for(int id : items) {
                images.add(new SoftReference<Bitmap>(loadImage(id)));
            }
        }

        private Bitmap loadImage(int id) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id);
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, IMAGE_WIDTH, IMAGE_HEIGHT, true);
            bitmap.recycle();
            return scaled;
        }

        @Override
        public int getItemsCount() {
            return items.length;
        }

        @Override
        public View getItem(int index, View convertView, ViewGroup parent) {

            ImageView img;
            if(convertView != null) {
                img = (ImageView) convertView;
            } else {
                img = new ImageView(context);
            }
            img.setLayoutParams(params);
            SoftReference<Bitmap> bitmapRef = images.get(index);
            Bitmap bitmap = bitmapRef.get();
            if(bitmap == null) {
                bitmap = loadImage(items[index]);
                images.set(index, new SoftReference<Bitmap>(bitmap));
            }
            img.setImageBitmap(bitmap);

            return img;
        }
    }

}
