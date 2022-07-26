package com.example.android.notificationwithservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String MESSAGE_KEY = "MESSAGE_KEY";
    public static final String MESSAGE_ACTION = "MESSAGE_ACTION";
    private boolean mBound  =false;
    private static final String TAG = "MyService";
    TextView textView;

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String s = intent.getStringExtra(MESSAGE_KEY);
            textView.setText(s);
        }
    };

    MyService myService;
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MyService.MyBinder myBinder = (MyService.MyBinder) iBinder;
            myService = myBinder.getMyService();
            mBound = true;
            Log.d(TAG, "onServiceConnected: ");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBound = false;
        }
    };

    public static final String MY_ACTION = "MY_ACTION";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(view -> {
            startService(new Intent(this,MyService.class));
            textView = findViewById(R.id.textView);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this,MyService.class);
        bindService(intent,serviceConnection,BIND_AUTO_CREATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,new IntentFilter(MESSAGE_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound){
            unbindService(serviceConnection);
            mBound = false;
            LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        }
    }

}