package com.qublish.backgroundClock.model;

public interface BackgroundClockService {

    /**
     * Called when the date string setted
     *
     * @param date The new long date passed from javascript interface
     */
    void setTime(long date);

    /**
     * Called when the get date in string
     */
    long getTime();

    /**
     * Called when the stopped the timer
     */
    void finish();

    /**
     * Called when the get status of timer
     */
    boolean getStatus();

    /**
     * Called when the update time
     * @param newTime The new long date passed from javascript interface
     */
    void updateTime(long newTime);


}
