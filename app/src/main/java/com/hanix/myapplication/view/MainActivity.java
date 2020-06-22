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
import com.hanix.myapplication.common.app.GLog;
import com.hanix.myapplication.view.widget.OnWheelChangedListener;
import com.hanix.myapplication.view.widget.OnWheelScrollListener;
import com.hanix.myapplication.view.widget.WheelView;
import com.hanix.myapplication.view.widget.adapter.AbstractWheelAdapter;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private boolean isFirWheelStop, isSecWheelStop, isThrWheelStop = true;
    private boolean isWheelRunning = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initWheel(R.id.mainSlot1);
        initWheel(R.id.mainSlot2);
        initWheel(R.id.mainSlot3);

        ImageView mix = (ImageView)findViewById(R.id.mainBt);
        mix.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                isFirWheelStop = false;
                isWheelRunning = true;
                mixWheel(R.id.mainSlot1);
            }
        });

        updateStatus();
    }

    private boolean wheelScrolled = false;

    OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
        public void onScrollingStarted(WheelView wheel) {
            wheelScrolled = true;
        }
        public void onScrollingFinished(WheelView wheel) {
            wheelScrolled = false;
            updateStatus();
        }
    };

    private OnWheelChangedListener changedListener = new OnWheelChangedListener() {
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            if (!wheelScrolled) {
                updateStatus();
            }
        }
    };

    private void updateStatus() {
        if(isWheelRunning) {
            if(!isFirWheelStop)  {
                isFirWheelStop = true;
                isSecWheelStop = false;
                mixWheel(R.id.mainSlot2);
            } else if(!isSecWheelStop) {
                isSecWheelStop = true;
                isThrWheelStop = false;
                mixWheel(R.id.mainSlot3);
            } else if(!isThrWheelStop) {
                isThrWheelStop = true;
                isWheelRunning = false;
                getResult();
            }
        }
    }

    private void getResult() {
        GLog.d("First Result : " + result(getWheel(R.id.mainSlot1).getCurrentItem()));
        GLog.d("Second Result : " + result(getWheel(R.id.mainSlot2).getCurrentItem()));
        GLog.d("Third Result : " + result(getWheel(R.id.mainSlot3).getCurrentItem()));
    }

    private String result(int index) {
        String returnString = "";
        switch (index) {
            case 0 :
                returnString = "곰";
                break;

            case 1 :
                returnString = "소";
                break;

            case 2 :
                returnString = "여우";
                break;

            case 3 :
                returnString = "사자";
                break;

            case 4:
                returnString = "쥐";
                break;

            case 5 :
                returnString = "개";
                break;

            case 6 :
                returnString = "돌고래";
                break;

            case 7 :
                returnString = "코알라";
                break;

            case 8 :
                returnString = "북극곰";
                break;

            case 9 :
                returnString = "토끼";
                break;

            case 10 :
                returnString = "순록";
                break;

            case 11 :
                returnString = "고래";
                break;

        }
        return returnString;
    }

    private void initWheel(int id) {
        WheelView wheel = getWheel(id);
        wheel.setViewAdapter(new SlotMachineAdapter(this));
        wheel.setCurrentItem((int)(Math.random() * 10));
        wheel.setVisibility(3);
        wheel.setBackgroundColor(Color.TRANSPARENT);
        wheel.setOutlineSpotShadowColor(Color.TRANSPARENT);
        wheel.setOutlineAmbientShadowColor(Color.TRANSPARENT);
        wheel.setForegroundGravity(Gravity.CENTER);

        wheel.addChangingListener(changedListener);
        wheel.addScrollingListener(scrolledListener);
        wheel.setCyclic(true);
        wheel.setEnabled(false);
    }

    private WheelView getWheel(int id) {
        return (WheelView) findViewById(id);
    }




    private void mixWheel(int id) {
        WheelView wheel = getWheel(id);
        wheel.scroll(-350 + (int)(Math.random() * 50), 1500);
    }

    private class SlotMachineAdapter extends AbstractWheelAdapter {
        final int IMAGE_WIDTH = 100;
        final int IMAGE_HEIGHT = 100;

        private final int items[] = new int[] {
                R.drawable.bear,
                R.drawable.cow,
                R.drawable.fox,
                R.drawable.lion,
                R.drawable.mouse,
                R.drawable.dog,
                R.drawable.dolphin,
                R.drawable.koala,
                R.drawable.polar_bear,
                R.drawable.rabbit,
                R.drawable.reindeer,
                R.drawable.whale
        };

        private List<SoftReference<Bitmap>> images;
        private Context context;

        public SlotMachineAdapter(Context context) {
            this.context = context;
            images = new ArrayList<SoftReference<Bitmap>>(items.length);
            for (int id : items) {
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

        final ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(IMAGE_WIDTH, IMAGE_HEIGHT);

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            ImageView img;
            if (cachedView != null) {
                img = (ImageView) cachedView;
            } else {
                img = new ImageView(context);
            }
            img.setLayoutParams(params);
            SoftReference<Bitmap> bitmapRef = images.get(index);
            Bitmap bitmap = bitmapRef.get();
            if (bitmap == null) {
                bitmap = loadImage(items[index]);
                images.set(index, new SoftReference<Bitmap>(bitmap));
            }
            img.setImageBitmap(bitmap);

            return img;
        }
    }
}
