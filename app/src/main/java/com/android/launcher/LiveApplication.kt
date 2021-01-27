package com.android.launcher

import android.app.Application
import com.android.launcher.livemonitor.api.NaoManager
import com.android.launcher.livemonitor.common.BuildType
import sm.utils.UtilsInitializer

public class LiveApplication : Application() {
    companion object{
        var liveApplication:LiveApplication?=null;
    }
    override fun onCreate() {
        super.onCreate()
        liveApplication=this;
        NaoManager.init(this, BuildType.RELEASE)
        UtilsInitializer.init(this)
    }
}