package com.android.launcher.livemonitor.setting

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.launcher.R
import com.android.launcher.livemonitor.dialog.RemindDialog
import kotlinx.android.synthetic.main.activity_net_work_setting.*

class NetWorkSettingActivity : AppCompatActivity() {

    private val dialog by lazy { RemindDialog(this, "是否重置网络设置？") }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_net_work_setting)
        ll_back.setOnClickListener { finish() }
        tv_reset.visibility = View.GONE
        tv_reset.setOnClickListener {
            dialog.show()
        }

        rl_wifi.setOnClickListener {
            val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
            intent.putExtra("extra_prefs_show_button_bar", true) //是否显示button bar
            intent.putExtra("extra_prefs_set_next_text", "完成")
            intent.putExtra("extra_prefs_set_back_text", "返回")
            startActivity(intent)
        }
        rl_yitaiwang.setOnClickListener {
            val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
            intent.putExtra("extra_prefs_show_button_bar", true) //是否显示button bar
            intent.putExtra("extra_prefs_set_next_text", "完成")
            intent.putExtra("extra_prefs_set_back_text", "返回")
            startActivity(intent)
        }
    }
}