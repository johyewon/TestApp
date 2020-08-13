package com.hanix.myapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hanix.myapplication.R;
import com.hanix.myapplication.view.event.OnSingleClickListener;

public class NetworkNotConnectionActivity extends AppCompatActivity {

    TextView networkTv;
    Button networkBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_not_connection);

        networkBt = findViewById(R.id.networkBt);
        networkTv = findViewById(R.id.networkTv);

        networkTv.setOnClickListener(networkClick);
        networkBt.setOnClickListener(networkClick);
    }

    private OnSingleClickListener networkClick = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            Intent intent = new Intent(NetworkNotConnectionActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };

}