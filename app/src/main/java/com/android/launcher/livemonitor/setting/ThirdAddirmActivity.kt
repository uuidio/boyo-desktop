package com.android.launcher.livemonitor.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.launcher.R
import kotlinx.android.synthetic.main.comment_title.*

class ThirdAddirmActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third_addirm)
        ll_back.setOnClickListener { finish() }
        tv_title.setText("第三方平台使用申明")
    }
}