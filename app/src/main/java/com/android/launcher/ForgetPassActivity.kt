package com.android.launcher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import kotlinx.android.synthetic.main.activity_forget_pass.*

class ForgetPassActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_pass)

        tv_send_confirm.setOnClickListener {
            if (et_user.text.isNotEmpty()){
                tv_send_confirm.isEnabled=false
                tv_send_confirm.text="60s"
                object:CountDownTimer(60*1000,1000){
                    override fun onFinish() {
                       tv_send_confirm.text="发送验证码"
                        tv_send_confirm.isEnabled=false
                    }

                    override fun onTick(millisUntilFinished: Long) {
                        tv_send_confirm.text=(millisUntilFinished/1000).toInt().toString()+"s"
                    }

                }.start()
            }
        }

        iv_back.setOnClickListener {
            finish()
        }
    }
}