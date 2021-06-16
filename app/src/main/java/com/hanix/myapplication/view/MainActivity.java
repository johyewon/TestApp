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
import com.hanix.myapplication.view.slot.kakaomap.MapTestFragment;
import com.hanix.myapplication.view.slot.local.LocalIPFragment;
import com.hanix.myapplication.view.slot.slotmachine.SlotMachineFragment;
import com.hanix.myapplication.view.slot.sns.SnsLoginActivity;
import com.hanix.myapplication.view.slot.text.ClearEditTextFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static String[] getRequestPermissions() {
        return new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };
    }

    /**
     * 프래그먼트
     **/
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private SlotMachineFragment slotMachineFragment;
    private MapTestFragment mapTestFragment;
    private LocalIPFragment localIPFragment;
    private ClearEditTextFragment clearEditTextFragment;

    // BindView
    ImageView menu;
    TextView logo;
    ConstraintLayout tab;
    FrameLayout container;
    LinearLayout mainLayout;

    // 상단 메뉴(햄버거바) 시작
    View tabView;
    RecyclerView tabSlotRecyclerView;
    LinearLayout tabLayout;

    MenuAdapter menuAdapter;
    List<String> items;
    // 상단 메뉴(햄버거바) 끝


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menu = findViewById(R.id.menu);
        logo = findViewById(R.id.logo);
        tab = findViewById(R.id.tab);
        container = findViewById(R.id.container);
        mainLayout = findViewById(R.id.mainLayout);

        ActivityCompat.requestPermissions(this, getRequestPermissions(), 100);

        // Click Event
        menu.setOnClickListener(mainClick);
        logo.setOnClickListener(mainClick);
        tab.setOnClickListener(mainClick);

        fragmentManager = getSupportFragmentManager();

        slotMachineFragment = new SlotMachineFragment();
        mapTestFragment = new MapTestFragment();
        localIPFragment = new LocalIPFragment();
        clearEditTextFragment = new ClearEditTextFragment();

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, slotMachineFragment).commitAllowingStateLoss();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100 && grantResults.length == getRequestPermissions().length) {

            boolean checkResult = false;
            String resultMsg = "";

            for (int i = 0; i < grantResults.length; i++) {
                int result = grantResults[i];
                if (result != PackageManager.PERMISSION_GRANTED) {
                    checkResult = true;
                    resultMsg = permissions[i];
                    break;
                }
            }

            if (checkResult) {
                String errMsg = resultMsg + " 권한이 거부되어 앱을 실행할 수 없습니다. 설정에서 권한을 허용하고 다시 실행해 주십시오.";
                GLog.d(errMsg);
                DlgUtil.showConfirmDlg(this, errMsg, false, (dialogInterface, i) -> {
                });
            }
        }
    }

    private final OnSingleClickListener mainClick = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            showHamburger();
        }
    };

    /**
     * 햄버거 메뉴 보여주기
     */
    private void showHamburger() {
        Dialog hamburger = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        hamburger.setContentView(R.layout.dialog_tab_menu);

        Objects.requireNonNull(hamburger.getWindow()).getAttributes().windowAnimations = R.style.SlideLeftStyle;
        hamburger.getWindow().setBackgroundDrawableResource(R.color.transparent);
        hamburger.getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        hamburger.getWindow().getAttributes().height = WindowManager.LayoutParams.MATCH_PARENT;

        tabView = hamburger.findViewById(R.id.tabView);
        tabLayout = hamburger.findViewById(R.id.tabLayout);
        tabSlotRecyclerView = hamburger.findViewById(R.id.tabSlotRecyclerView);

        items = new ArrayList<>();

        // slot 추가
        items.add("카지노 룰렛 휠");
        items.add("SNS 로그인");
        items.add("카카오주소 API");
        items.add("ip 주소");
        items.add("Clear EditText");

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

    /**
     * 선택한 옵션에 따라  fragment 변경
     *
     * @param value
     */
    private void setContainer(String value) {
        Fragment fragment;
        switch (value) {
            case "카지노 룰렛 휠":
                fragment = fragmentManager.getPrimaryNavigationFragment();
                if (transaction != null) {
                    transaction = fragmentManager.beginTransaction();
                    if (fragment != null)
                        transaction.remove(fragment);
                    transaction.replace(R.id.container, slotMachineFragment).commitAllowingStateLoss();
                }
                break;

            case "SNS 로그인":
                Intent intent = new Intent(MainActivity.this, SnsLoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in_activity, R.anim.hold_activity);
                break;

            case "카카오주소 API":
                fragment = fragmentManager.getPrimaryNavigationFragment();
                if (transaction != null) {
                    transaction = fragmentManager.beginTransaction();
                    if (fragment != null)
                        transaction.remove(fragment);
                    transaction.replace(R.id.container, mapTestFragment).commitAllowingStateLoss();
                }
                break;

            case "ip 주소":
                fragment = fragmentManager.getPrimaryNavigationFragment();
                if (transaction != null) {
                    transaction = fragmentManager.beginTransaction();
                    if (fragment != null)
                        transaction.remove(fragment);
                    transaction.replace(R.id.container, localIPFragment).commitAllowingStateLoss();
                }
                break;

            case "Clear EditText":
                fragment = fragmentManager.getPrimaryNavigationFragment();
                if (transaction != null) {
                    transaction = fragmentManager.beginTransaction();
                    if (fragment != null)
                        transaction.remove(fragment);
                    transaction.replace(R.id.container, clearEditTextFragment).commitAllowingStateLoss();
                }
                break;

            default:
                break;
        }
    }

}
