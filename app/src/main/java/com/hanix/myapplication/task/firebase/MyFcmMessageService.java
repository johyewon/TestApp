package com.hanix.myapplication.task.firebase;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hanix.myapplication.R;
import com.hanix.myapplication.common.app.GLog;
import com.hanix.myapplication.common.utils.AppUtil;
import com.hanix.myapplication.common.utils.PrefUtil;
import com.hanix.myapplication.view.AppIntro;
import com.hanix.myapplication.view.MainActivity;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

public class MyFcmMessageService extends FirebaseMessagingService {

    private String title, body, imgUrl = "";
    private Intent resultIntent;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        FirebaseMessaging.getInstance().subscribeToTopic("CastingBox");
        GLog.d("Firebase Instance Id Service : " + s);
        PrefUtil.getInstance(this).setFcmTokenId(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        if(remoteMessage != null && remoteMessage.getData().size() > 0) {

            if(true) {
                sceduleJob();
            } else {
                handleNow();
            }
        }

        if(remoteMessage.getNotification() != null) {
            GLog.d("Message Notification Body : " + remoteMessage.getNotification().getColor());
            GLog.d("Message Notification Body : " + remoteMessage.getNotification().getIcon());
            GLog.d("Message Notification Body : " + remoteMessage.getNotification().getTitle());
            GLog.d("Message Notification Body : " + remoteMessage.getNotification().getBody());

            if(remoteMessage.getData().get("body") != null && remoteMessage.getData().get("body").length() > 0) {
                title = remoteMessage.getData().get("title");
                body = remoteMessage.getData().get("body");
                imgUrl = remoteMessage.getData().get("imgUrl");
            } else {
                title = remoteMessage.getNotification().getTitle();
                body = remoteMessage.getNotification().getBody();
                imgUrl = String.valueOf(remoteMessage.getNotification().getImageUrl());
            }
            sendNotification();
        }
    }

    private void sceduleJob() {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag("my-job-tag")
                .build();
        dispatcher.schedule(myJob);
    }

    private void handleNow() {
        GLog.d("Short lived task is done.");
    }

    /** 메시지가 수신되었을 때 실행되는 메소드**/
    private void sendNotification() {

        // 어플 실행 중인지 확인해서 실행 중이면 Main으로 보내기
        if(AppUtil.isAppRunning(this)) {
            resultIntent = new Intent(this, MainActivity.class);
        } else {
            resultIntent = new Intent(this, AppIntro.class);
        }

        resultIntent.putExtra("title", title);
        resultIntent.putExtra("text", body);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String channelId = getResources().getString(R.string.default_notification_channel_id);
        String channel_nm = getResources().getString(R.string.default_notification_channel_name); // 앱 설정에서 알림 이름으로 뜸.
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.alert_msg_layout);
        remoteViews.setImageViewResource(R.id.alertIcon, R.drawable.ic_launcher_foreground);
        if(title != null && title.length() > 0)
            remoteViews.setTextViewText(R.id.alertTitle, title);

        if(body != null && body.length() > 0)
            remoteViews.setTextViewText(R.id.alertContent, body);

        if(imgUrl.equals("") && imgUrl.length() == 0) {
            remoteViews.setViewVisibility(R.id.alertSendImg, View.GONE);
        } else {
            remoteViews.setViewVisibility(R.id.alertSendImg, View.VISIBLE);
            try {
                URL url = new URL(imgUrl);
                URLConnection conn = url.openConnection();
                conn.connect();

                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                Bitmap img = BitmapFactory.decodeStream(bis);

                remoteViews.setImageViewBitmap(R.id.alertSendImg, img);
            } catch (Exception e) {
                GLog.e(e.getMessage(), e);
            }
        }

        NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(this, channelId)
                .setContent(remoteViews)
                .setCustomContentView(remoteViews)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground))
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(body)
                .setWhen(System.currentTimeMillis())
                .setSound(defaultSoundUri)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setVibrate(new long[]{1000, 1000})
                .setContentIntent(pendingIntent)
                .setFullScreenIntent(pendingIntent, true)
                .setPriority(Notification.PRIORITY_HIGH);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT  >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channel_nm, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Casting Box");
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setShowBadge(true);
            channel.setVibrationPattern(new long[]{100, 200, 100, 200});
            if(notificationManager != null )
                notificationManager.createNotificationChannel(channel);

            notiBuilder.setNumber(100);
            notiBuilder.setChannelId(channelId);
        }

        PowerManager  pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if(pm != null) {
            @SuppressLint("InvalidWakeLockTag")
            WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "CastingBox");
            wakeLock.acquire(3000);
        }

        if(notificationManager != null) {
            notificationManager.notify(0, notiBuilder.build());
        }
    }

}
