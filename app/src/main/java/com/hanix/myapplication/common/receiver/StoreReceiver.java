package com.hanix.myapplication.common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StoreReceiver extends BroadcastReceiver {

    static String returnReferrer = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        String referrer = intent.getStringExtra("referrer");
        if(referrer != null && referrer.length() > 0) {
            returnReferrer = referrer;
        }
    }

    public static String getReferrer() {
        return returnReferrer;
    }
}
