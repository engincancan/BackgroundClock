package com.qublish.backgroundClock.service;


import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.qublish.backgroundClock.model.BackgroundClockService;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class BackgroundClockServiceImpl extends Service implements BackgroundClockService {
    /**
     * indicates how to behave if the service is killed
     */
    private int mStartMode = START_NOT_STICKY;
    private static final String TAG = "BackgroundClockService";
    private int interval;
    private Date currentTime = new Date();
    private SharedPreferences configService;
    private boolean isResume = true;
    private ServiceBinder binder = new ServiceBinder();
    private Timer timer = new Timer();

    public BackgroundClockServiceImpl() {
    }

    @Override
    public void onCreate() {
        configService = getSharedPreferences("BgClockService",
                MODE_PRIVATE);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "Background Service Started");
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                interval = bundle.getInt("interval", 1000);
                setTime(bundle.getLong("currentTime", new Date().getTime()));
                isResume = true;
            } else {
                Log.d(TAG, "bundle is empty");
            }
        } else {
            Log.d(TAG, "intent is empty");
        }
        return mStartMode;
    }

    @Override
    public void onDestroy() {

        // Start BackgroundClockService Again
        int _interval = interval;
        SharedPreferences.Editor configEditor = configService.edit();
        Long cT = currentTime.getTime();
        configEditor.putLong("currentTime", cT);
        configEditor.putLong("interval", _interval);
        configEditor.commit();
        sendBroadcast(new Intent("RestartingBgClockService"));
        isResume = false;
        super.onDestroy();
        Log.e(TAG, "Background Service Destroyed");
    }

    /*
     * @see android.app.Service#onBind(android.content.Intent)
     */
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.v(TAG, "in onRebind");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.v(TAG, "in onUnbind");
        return true;
    }

    public class ServiceBinder extends android.os.Binder {
        public BackgroundClockServiceImpl getService() {
            return BackgroundClockServiceImpl.this;
        }
    }

    public void setTime(long time) {
        if (isResume) {
            currentTime.setTime(time);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    setTime(currentTime.getTime() + interval);
                    Log.d(TAG, "run: " + currentTime.toLocaleString());
                }
            }, interval);
        }

    }

    public long getTime() {
        return currentTime.getTime();
    }

    public void updateTime(long newTime) {
        if(!isResume){
            isResume = true;
            setTime(newTime);
        } else {
            currentTime.setTime(newTime);
        }
    }


    public void finish() {
        this.getApplicationContext().stopService(new Intent(this, BackgroundClockServiceImpl.class));
        stopSelf();
        isResume = false;
    }

    public boolean getStatus() {
        return isResume;
    }
}
