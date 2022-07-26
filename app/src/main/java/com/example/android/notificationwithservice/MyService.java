package com.example.android.notificationwithservice;

import static com.example.android.notificationwithservice.App.CHANNEL_ID;
import static com.example.android.notificationwithservice.MainActivity.MESSAGE_ACTION;
import static com.example.android.notificationwithservice.MainActivity.MESSAGE_KEY;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MyService extends IntentService {

    private static final String TAG = "MyService";
    MyBinder myBinder = new MyBinder();

    public MyService() {
        super("my");
        setIntentRedelivery(false);
    }


    class MyBinder extends Binder {
        public MyService getMyService() {
            return MyService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return myBinder;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        showNotification();
        Log.d(TAG, "onHandleIntent: ");
        SystemClock.sleep(10000);
        Intent intent1 = new Intent(MESSAGE_ACTION);
        intent1.putExtra(MESSAGE_KEY,"MESSAGE BROADCAST : DONE");
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent1);
        stopForeground(true);
        stopSelf();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: ");
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG, "onRebind: ");
        super.onRebind(intent);
    }



    //
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//
//        showNotification();
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                SystemClock.sleep(10000);
//                stopForeground(true);
//                stopSelf();
//            }
//        });
//        thread.start();
//
//        return START_NOT_STICKY;
//    }

    private void showNotification() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setContentTitle("Service");
        builder.setContentText("this service is running");
        builder.setSmallIcon(R.drawable.ic_launcher_background);

        Notification notification = builder.build();
        startForeground(123, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");

    }
}