package com.android.launcher.livemonitor.setting

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.TelephonyManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.launcher.R
import kotlinx.android.synthetic.main.activity_about_native.*
import sm.utils.AppUtils
import sm.utils.DeviceUtils

class AboutNativeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_native)
        ll_back.setOnClickListener { finish() }
        tv_mac.text = DeviceUtils.getMacAddress()
        tv_soft_version.text = "ys_" + AppUtils.getAppVersionName().toString()
        tv_haw_version.text = "ys_1.0.0"
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        tv_imei.text = (getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).subscriberId
    }
}