package com.hanix.myapplication.view.slot.kakaomap;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.hanix.myapplication.R;
import com.hanix.myapplication.view.event.OnSingleClickListener;

public class MapTestFragment extends Fragment {

    private WebView mapWebView;
    @SuppressLint("StaticFieldLeak")
    static TextView mapTextView;
    private Button mapFindBtn;
    static Dialog dialog;

    public MapTestFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map_test, container, false);

        mapTextView = view.findViewById(R.id.mapTextView);
        mapFindBtn = view.findViewById(R.id.mapFindBtn);

        mapFindBtn.setOnClickListener(mapClick);

        return view;
    }

    OnSingleClickListener mapClick = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            switch (v.getId()) {
                case R.id.mapFindBtn :
                    showMapDialog();
                    break;


                case R.id.mapTextView:
                default :
                    break;
            }
        }
    };

    @SuppressLint("SetJavaScriptEnabled")
    private void showMapDialog() {
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_daum_map);
        dialog.getWindow().getAttributes().windowAnimations = R.style.PushUpStyle;

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.height = LinearLayout.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(params);

        mapWebView = dialog.findViewById(R.id.mapWebView);
        mapWebView.addJavascriptInterface(new MyJavaScriptInterface(), "Android");
        mapWebView.getSettings().setJavaScriptEnabled(true);

        mapWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                mapWebView.loadUrl("javascript:sample2_execDaumPostcode();");
            }
        });

        mapWebView.loadUrl("http://www.inspond.com/daum.html");

        dialog.show();

    }

    static class MyJavaScriptInterface {

        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processDATA(String data) {
            new Handler().post(() -> {
                mapTextView.setText(data);
                if(dialog != null && dialog.isShowing())
                    dialog.dismiss();
            });
        }
    }
}