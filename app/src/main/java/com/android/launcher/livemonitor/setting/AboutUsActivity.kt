package com.android.launcher.livemonitor.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.launcher.R
import kotlinx.android.synthetic.main.activity_about_us.*

class AboutUsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)
        ll_back.setOnClickListener { finish() }

//        rl_live.setOnClickListener { startActivity(Intent(this,YSLiveActivity::class.java)) }
//        rl_affirm.setOnClickListener { startActivity(Intent(this,ThirdAddirmActivity::class.java)) }
    }
}