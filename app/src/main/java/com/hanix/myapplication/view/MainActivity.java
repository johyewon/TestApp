package com.hanix.myapplication.view;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hanix.myapplication.R;
import com.hanix.myapplication.common.app.GLog;
import com.hanix.myapplication.common.utils.DlgUtil;
import com.hanix.myapplication.view.adapter.MenuAdapter;
import com.hanix.myapplication.view.event.OnSingleClickListener;
import com.hanix.myapplication.view.slot.SlotMachineFragment;
import com.hanix.myapplication.view.slot.SnsLoginActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static String[] getRequestPermissions() {
        return new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };
    }

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

        ActivityCompat.requestPermissions(this, getRequestPermissions(), 100);

        // Click Event
        menu.setOnClickListener(mainClick);
        logo.setOnClickListener(mainClick);
        tab.setOnClickListener(mainClick);
        fragmentManager = getSupportFragmentManager();

        slotMachineFragment = new SlotMachineFragment();

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, slotMachineFragment).commitAllowingStateLoss();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100 && grantResults.length == getRequestPermissions().length) {

            boolean checkResult = false;
            String resultMsg = "";

            for(int i = 0; i < grantResults.length; i++) {
                int result = grantResults[i];
                if(result != PackageManager.PERMISSION_GRANTED) {
                    checkResult = true;
                    resultMsg = permissions[i];
                    break;
                }
            }

            if(checkResult) {
                String errMsg = resultMsg + " 권한이 거부되어 앱을 실행할 수 없습니다. 설정에서 권한을 허용하고 다시 실행해 주십시오.";
                GLog.d(errMsg);
                DlgUtil.showConfirmDlg(this, errMsg, false, (dialogInterface, i) -> {});
            }
        }
    }

    private OnSingleClickListener mainClick = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            if (v.getId() == R.id.menu) {
                showHamburger();
            }
        }
    };

    private void showHamburger() {
        Dialog hamburger = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        hamburger.setContentView(R.layout.dialog_tab_menu);
        Objects.requireNonNull(hamburger.getWindow()).getAttributes().windowAnimations = R.style.SlideLeftStyle;
        hamburger.getWindow().setBackgroundDrawableResource(R.color.transparent);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(hamburger.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;

        tabView = hamburger.findViewById(R.id.tabView);
        tabSlotRecyclerView = hamburger.findViewById(R.id.tabSlotRecyclerView);
        tabLayout = hamburger.findViewById(R.id.tabLayout);

        items = new ArrayList<>();
        items.add("카지노 룰렛 휠");
        items.add("SNS 로그인");
        menuAdapter = new MenuAdapter(items, getApplicationContext());
        menuAdapter.setItemClick((view, position) -> {
            hamburger.dismiss();
            setContainer(String.valueOf(menuAdapter.getItem(position)));
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
                if(transaction != null) {
                    transaction = fragmentManager.beginTransaction();
                    if(fragment != null)
                        transaction.remove(fragment);
                    transaction.replace(R.id.container, slotMachineFragment).commitAllowingStateLoss();
                }
                break;

            case "SNS 로그인" :
                Intent intent = new Intent(MainActivity.this, SnsLoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in_activity, R.anim.hold_activity);
                break;

            default:
                break;
        }
    }

}
