package com.android.launcher

import android.app.Application
import com.android.launcher.livemonitor.api.NaoManager
import com.android.launcher.livemonitor.common.BuildType
import sm.utils.UtilsInitializer

public class LiveApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NaoManager.init(this, BuildType.DEBUG)
        UtilsInitializer.init(this)
    }
}