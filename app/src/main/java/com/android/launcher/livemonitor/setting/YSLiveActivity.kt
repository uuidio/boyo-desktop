package com.android.launcher.livemonitor.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.launcher.R
import kotlinx.android.synthetic.main.comment_title.*

class YSLiveActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_y_s_live)
        ll_back.setOnClickListener { finish() }
        tv_title.setText("优视直播")
    }
}