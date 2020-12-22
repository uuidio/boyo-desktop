package com.android.launcher.livemonitor.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.launcher.livemonitor.service.APTService;

/**
 * Created on 2020/12/3.
 *
 * @author Simon
 */
public class BootReceiver extends BroadcastReceiver {
    private String LOG = "BootReceiver";

    @Override

    public void onReceive(Context arg0, Intent arg1) {

        Log.i(LOG, "onReceive");

        Intent mBootIntent = new Intent(arg0, APTService.class);
        arg0.startService(mBootIntent);
    }

}

