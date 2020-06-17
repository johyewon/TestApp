package com.hanix.myapplication.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.hanix.myapplication.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void resetBottomBtns() {
//        if(mWebView == null) return;
//
//        if(mWebView.canGoBack()) {
//            mBtnPrev.setImageResource(R.drawable.ic_action_previous_item_bold);
//        }
//        else {
//            mBtnPrev.setImageResource(R.drawable.ic_action_previous_item);
//        }
//
//        if(mWebView.canGoForward()) {
//            mBtnNext.setImageResource(R.drawable.ic_action_next_item_bold);
//        }
//        else {
//            mBtnNext.setImageResource(R.drawable.ic_action_next_item);
//        }
//
    }

}
