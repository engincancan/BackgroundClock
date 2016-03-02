package com.qublish.backgroundClock;

import android.os.CountDownTimer;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class echoes a string called from JavaScript.
 */
public class BackgroundClock extends CordovaPlugin {

    private static final String TAG = "BackgroundClock";
    private Calendar calendar;
    private Date date = new Date();
    private static final String INIT = "init";
    private static final String SET_TIME = "settime";
    private CountDownTimer cdt;
    private static Timer timer = new Timer();


    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals(this.INIT)) {
            calendar = (Calendar) Calendar.getInstance().clone();
            String message = args.getString(0);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            try {
                calendar.setTime(format.parse(message));
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        calendar.add(Calendar.MILLISECOND, 1000);
                    }
                }, 0, 1000);
                Log.d(TAG, "execute: settime");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            callbackContext.success("Success");
            return true;
        } else if (action.equals("finish")) {
            timer.cancel();
        } else if( action.equals("gettime")){
            callbackContext.success(calendar.getTime().toString());
            return true;
        }
        callbackContext.success("Fail");
        return false;
    }

    private void init(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
}
