package com.hanix.myapplication.view.slot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.hanix.myapplication.R;
import com.hanix.myapplication.common.app.GLog;
import com.hanix.myapplication.view.widget.wheel.OnWheelChangedListener;
import com.hanix.myapplication.view.widget.wheel.OnWheelScrollListener;
import com.hanix.myapplication.view.widget.wheel.WheelView;
import com.hanix.myapplication.view.widget.wheel.adapter.AbstractWheelAdapter;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

public class SlotMachineFragment extends Fragment {

    private boolean isFirWheelStop, isSecWheelStop, isThrWheelStop = true;
    private boolean wheelScrolled = false;
    private boolean isWheelRunning = false;

    private final int[] items1 = new int[]{
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
    private final int[] items2 = new int[]{
            R.drawable.bat,
            R.drawable.bee,
            R.drawable.bird,
            R.drawable.butterfly,
            R.drawable.camel,
            R.drawable.cat,
            R.drawable.chameleon,
            R.drawable.chicken,
            R.drawable.clownfish,
            R.drawable.crab,
            R.drawable.crocodile,
            R.drawable.duck
    };
    private final int[] items3 = new int[]{
            R.drawable.elephant,
            R.drawable.flamingo,
            R.drawable.frog,
            R.drawable.giraffe,
            R.drawable.hippopotamus,
            R.drawable.horse,
            R.drawable.kangaroo,
            R.drawable.llama,
            R.drawable.manta_ray,
            R.drawable.monkey,
            R.drawable.owl,
            R.drawable.panther
    };

    ViewGroup rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_slot_machine, container, false);

        initWheel(R.id.mainSlot1, items1);
        initWheel(R.id.mainSlot2, items2);
        initWheel(R.id.mainSlot3, items3);

        ImageView mix = rootView.findViewById(R.id.mainBt);
        mix.setOnClickListener((v) -> {
            isFirWheelStop = false;
            isWheelRunning = true;
            mixWheel(R.id.mainSlot1);
        });
        updateStatus();

        return rootView;
    }

    OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
        public void onScrollingStarted(WheelView wheel) {
            wheelScrolled = true;
        }

        public void onScrollingFinished(WheelView wheel) {
            wheelScrolled = false;
            updateStatus();
        }
    };

    private OnWheelChangedListener changedListener = (wheel, oldValue, newValue) -> {
        if (!wheelScrolled)
            updateStatus();
    };

    private void updateStatus() {
        if (isWheelRunning) {
            if (!isFirWheelStop) {
                isFirWheelStop = true;
                isSecWheelStop = false;
                mixWheel(R.id.mainSlot2);
            } else if (!isSecWheelStop) {
                isSecWheelStop = true;
                isThrWheelStop = false;
                mixWheel(R.id.mainSlot3);
            } else if (!isThrWheelStop) {
                isThrWheelStop = true;
                isWheelRunning = false;
                getResult();
            }
        }
    }

    private void getResult() {
        GLog.d("First Result : " + result(1, getWheel(R.id.mainSlot1).getCurrentItem()));
        GLog.d("Second Result : " + result(2, getWheel(R.id.mainSlot2).getCurrentItem()));
        GLog.d("Third Result : " + result(3, getWheel(R.id.mainSlot3).getCurrentItem()));
    }

    private String result(int slot, int index) {
        String returnString = "";
        if (slot == 1) {
            switch (index) {
                case 0:
                    returnString = "곰";
                    break;

                case 1:
                    returnString = "소";
                    break;

                case 2:
                    returnString = "여우";
                    break;

                case 3:
                    returnString = "사자";
                    break;

                case 4:
                    returnString = "쥐";
                    break;

                case 5:
                    returnString = "개";
                    break;

                case 6:
                    returnString = "돌고래";
                    break;

                case 7:
                    returnString = "코알라";
                    break;

                case 8:
                    returnString = "북극곰";
                    break;

                case 9:
                    returnString = "토끼";
                    break;

                case 10:
                    returnString = "순록";
                    break;

                case 11:
                    returnString = "고래";
                    break;

            }
        } else if (slot == 2) {
            switch (index) {
                case 0:
                    returnString = "박쥐";
                    break;

                case 1:
                    returnString = "벌";
                    break;

                case 2:
                    returnString = "새";
                    break;

                case 3:
                    returnString = "나비";
                    break;

                case 4:
                    returnString = "낙타";
                    break;

                case 5:
                    returnString = "고양이";
                    break;

                case 6:
                    returnString = "카멜레온";
                    break;

                case 7:
                    returnString = "닭";
                    break;

                case 8:
                    returnString = "니모";
                    break;

                case 9:
                    returnString = "게";
                    break;

                case 10:
                    returnString = "악어";
                    break;

                case 11:
                    returnString = "오리";
                    break;

            }
        } else {
            switch (index) {
                case 0:
                    returnString = "코끼리";
                    break;

                case 1:
                    returnString = "플라밍고";
                    break;

                case 2:
                    returnString = "개구리";
                    break;

                case 3:
                    returnString = "기린";
                    break;

                case 4:
                    returnString = "하마";
                    break;

                case 5:
                    returnString = "말";
                    break;

                case 6:
                    returnString = "캥거루";
                    break;

                case 7:
                    returnString = "라마";
                    break;

                case 8:
                    returnString = "가오리";
                    break;

                case 9:
                    returnString = "원숭이";
                    break;

                case 10:
                    returnString = "부엉이";
                    break;

                case 11:
                    returnString = "표범";
                    break;

            }
        }
        return returnString;
    }

    private void initWheel(int id, int[] items) {
        WheelView wheel = getWheel(id);
        if (items != null && items.length > 0) {
            wheel.setViewAdapter(new SlotMachineAdapter(getContext(), items));
        }
        wheel.setCurrentItem((int) (Math.random() * 10));
        wheel.setVisibility(View.VISIBLE);
        wheel.setPadding(5, 5, 5, 5);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            wheel.setBackgroundColor(Color.TRANSPARENT);
            wheel.setForegroundGravity(Gravity.CENTER);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            wheel.setOutlineSpotShadowColor(Color.TRANSPARENT);
            wheel.setOutlineAmbientShadowColor(Color.TRANSPARENT);
        }
        wheel.addChangingListener(changedListener);
        wheel.addScrollingListener(scrolledListener);
        wheel.setCyclic(true);
        wheel.setVisibleItems(3);
        wheel.setEnabled(false);
    }

    private WheelView getWheel(int id) {
        return (WheelView) rootView.findViewById(id);
    }

    private void mixWheel(int id) {
        WheelView wheel = getWheel(id);
        wheel.scroll(-350 + (int) (Math.random() * 50), 1500);
    }

    private static class SlotMachineAdapter extends AbstractWheelAdapter {
        final int IMAGE_WIDTH = 150;
        final int IMAGE_HEIGHT = 150;
        int[] items;

        private List<SoftReference<Bitmap>> images;
        private Context context;

        public SlotMachineAdapter(Context context, int[] items) {
            this.context = context;
            this.items = items;
            images = new ArrayList<>(items.length);
            for (int id : items) {
                images.add(new SoftReference<>(loadImage(id)));
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
                images.set(index, new SoftReference<>(bitmap));
            }
            img.setImageBitmap(bitmap);

            return img;
        }
    }
}