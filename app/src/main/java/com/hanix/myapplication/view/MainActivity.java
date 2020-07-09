package com.hanix.myapplication.view;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hanix.myapplication.R;
import com.hanix.myapplication.view.adapter.MenuAdapter;
import com.hanix.myapplication.view.event.OnSingleClickListener;
import com.hanix.myapplication.view.slot.SlotMachineFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    /** 프래그먼트 **/
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private SlotMachineFragment slotMachineFragment;

    // BindView
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

    View tabView;
    RecyclerView tabSlotRecyclerView;
    LinearLayout tabLayout;

    MenuAdapter menuAdapter;
    List<String> items;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Click Event
        menu.setOnClickListener(mainClick);
        logo.setOnClickListener(mainClick);
        tab.setOnClickListener(mainClick);
        fragmentManager = getSupportFragmentManager();

        slotMachineFragment = new SlotMachineFragment();

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, slotMachineFragment).commitAllowingStateLoss();
    }

    private OnSingleClickListener mainClick = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            switch (v.getId()) {
                case R.id.menu:
                    showHamburger();
                    break;

                default:
                    break;
            }
        }
    };

    private void showHamburger() {
        Dialog hamburger = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        hamburger.setContentView(R.layout.dialog_tab_menu);
        hamburger.getWindow().getAttributes().windowAnimations = R.style.SlideLeftStyle;
        hamburger.getWindow().setBackgroundDrawableResource(R.color.transparent);
        hamburger.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(hamburger.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;

        tabView = hamburger.findViewById(R.id.tabView);
        tabSlotRecyclerView = hamburger.findViewById(R.id.tabSlotRecyclerView);
        tabLayout = hamburger.findViewById(R.id.tabLayout);

        items = new ArrayList<>();
        items.add("카지노 룰렛 휠");
        menuAdapter = new MenuAdapter(items, getApplicationContext());
        menuAdapter.setItemClick(new MenuAdapter.ItemClick() {
            @Override
            public void onClick(View view, int position) {
                hamburger.dismiss();
                setContainer(String.valueOf(menuAdapter.getItem(position)));
            }
        });
        tabSlotRecyclerView.setAdapter(menuAdapter);
        tabSlotRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        tabSlotRecyclerView.setItemAnimator(new DefaultItemAnimator());

        hamburger.show();

    }

    private void setContainer(String value) {
        switch (value) {
            case "카지노 룰렛 휠" :
                Fragment fragment = fragmentManager.getPrimaryNavigationFragment();
                if(fragment != null)
                    transaction.remove(fragment).commitAllowingStateLoss();
                transaction.replace(R.id.container, slotMachineFragment).commitAllowingStateLoss();
                break;

            default:
                break;
        }
    }

}
