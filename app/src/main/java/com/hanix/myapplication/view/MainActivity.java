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
import com.hanix.myapplication.view.event.OnSingleClickListener;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);



    }

    private OnSingleClickListener mainClick = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            switch (v.getId()) {
                case R.id.menu :
                    break;

                default:
                    break;
            }
        }
    };

}
