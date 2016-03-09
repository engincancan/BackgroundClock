package com.qublish.backgroundClock.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;


import com.example.hello.R;

import java.util.Date;


public class RestartBgClockService extends BroadcastReceiver {

    private SharedPreferences configService;


    @Override
    public void onReceive(Context context, Intent intent) {
        Context context1 = context.getApplicationContext();
        configService = context1.getSharedPreferences("BgClockService",
                context1.MODE_PRIVATE);
        Intent intent1 = new Intent(context1, BackgroundClockServiceImpl.class);
        intent1.putExtra("currentTime", configService.getLong("currentTime", new Date().getTime()));
        intent1.putExtra("interval", configService.getLong("interval", 1000));
        context.startService(intent1);
    }
}
