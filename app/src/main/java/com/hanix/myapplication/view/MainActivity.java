package com.hanix.myapplication.view;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.hanix.myapplication.R;
import com.hanix.myapplication.view.adapter.MenuAdapter;
import com.hanix.myapplication.view.event.OnSingleClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {


    @BindView(R.id.menu)
    ImageView menu;
    @BindView(R.id.logo)
    TextView logo;
    @BindView(R.id.tab)
    ConstraintLayout tab;
    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.mainLayout)
    LinearLayout mainLayout;

    private MenuAdapter menuAdapter;
    private static List<String> items;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        menu.setOnClickListener(mainClick);
        logo.setOnClickListener(mainClick);
        tab.setOnClickListener(mainClick);

    }

    private OnSingleClickListener mainClick = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            switch (v.getId()) {
                case R.id.menu :
                    showHamburger();
                    break;

                default:
                    break;
            }
        }
    };

    private void showHamburger() {

    }



}
