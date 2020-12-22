package com.android.launcher.livemonitor.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.launcher.ForegroundAppUtill;
import com.android.launcher.livemonitor.manager.WindowViewManager;

import androidx.annotation.Nullable;

public class APTService extends Service {
    private String LOG = "APTService";

    @Override
    public void onCreate() {
        super.onCreate();
        WindowViewManager.getViewManagerInstance().show(this);
        Log.i(LOG, "Oncreate");
        Toast.makeText(getApplicationContext(), LOG + "onCreate start!", Toast.LENGTH_LONG).show();
    }
    private Runnable r = new Runnable() {
        @Override
        public void run() {
            String foregroundActivityName = ForegroundAppUtill.getForegroundActivityName(getApplicationContext());
            Log.i("APTService--",foregroundActivityName);
            if ("com.android.launcher".equals(foregroundActivityName))
            {
                WindowViewManager.getViewManagerInstance().close();
            }else {
                WindowViewManager.getViewManagerInstance().show(getApplicationContext());
            }
            handler.postDelayed(r, 3000);
        }
    };
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(), LOG, Toast.LENGTH_LONG).show();

        return null;
    }

    @Override
    public void onDestroy() {
        WindowViewManager.getViewManagerInstance().close();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.postDelayed(r, 3000);
        return super.onStartCommand(intent, flags, startId);
    }

    private Handler handler = new Handler();
}
