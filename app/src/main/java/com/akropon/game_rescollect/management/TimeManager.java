package com.akropon.game_rescollect.management;

import com.akropon.game_rescollect.*;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;

/**
 * Created by akropon on 10.09.2017.
 */

public class TimeManager {

    public static long getNanoNow() {
        return System.nanoTime();
    }

    public static long getMillisNow() {
        return System.currentTimeMillis();
    }

    public static void sleepSeconds(float seconds) {
        try {
            Thread.sleep((long)(seconds*1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleepMillis(long millis) {
        try {
            Thread.sleep((long)millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void sleepUpToIntervalEndIfNeed(long startMomentMillis, long intervalDurationMillis) {
        long timeToSleepMillis = (startMomentMillis+intervalDurationMillis)-System.currentTimeMillis();
        if (timeToSleepMillis <= 1) return;
        try {
            Thread.sleep(timeToSleepMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void registerApplicationExitTimer (float seconds, final Engine engine) {
        engine.registerUpdateHandler(new TimerHandler(seconds, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                engine.unregisterUpdateHandler(pTimerHandler);
                System.exit(0);
            }
        }));
    }


}
