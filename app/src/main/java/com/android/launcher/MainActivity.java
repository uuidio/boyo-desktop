package com.android.launcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.android.launcher.livemonitor.manager.WindowViewManager;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private AppListPageView mAppListPageView = null;
    private AppChangeReceiver mAppChangeReceiver = null;

    private class AppChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mAppListPageView != null) {
                mAppListPageView.notifyAppChange();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WindowViewManager.getViewManagerInstance().setEnableDrag(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mAppListPageView = findViewById(R.id.applist_view);
        mAppListPageView.setVisibility(View.VISIBLE);
        mAppChangeReceiver = new AppChangeReceiver();
        IntentFilter filter1 = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
        filter1.addDataScheme("package");
        this.registerReceiver(mAppChangeReceiver, filter1);

        IntentFilter filter2 = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter2.addDataScheme("package");
        this.registerReceiver(mAppChangeReceiver, filter2);

        IntentFilter filter3 = new IntentFilter(Intent.ACTION_PACKAGE_REPLACED);
        filter3.addDataScheme("package");
        this.registerReceiver(mAppChangeReceiver, filter3);

    }

    @Override
    protected void onResume() {
        super.onResume();
        WindowViewManager.getViewManagerInstance().close();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mAppChangeReceiver);
        super.onDestroy();
    }
}
