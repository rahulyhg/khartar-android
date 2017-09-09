package com.vardhamaninfo.khartargaccha.Custom;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;

/**
 * Created by android1 on 7/3/16.
 */
public class CountTimerService extends CountDownTimer {

    public static CountDownTimer countDownTimer;
    Handler handler;


    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public CountTimerService(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        Log.d("onTick","onTick");

    }

    @Override
    public void onFinish() {
        Log.d("onFinish","onfinish");
        handler = new Handler();

        handler.post(new Runnable() {
            public void run() {
                Log.d("handler","handler");

            }
        });



    }
}
