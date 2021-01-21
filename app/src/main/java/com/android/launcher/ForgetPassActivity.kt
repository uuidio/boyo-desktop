package com.android.launcher

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.launcher.livemonitor.api.APIFactory
import com.android.launcher.livemonitor.api.entity.SendCodeRsp
import com.android.launcher.livemonitor.common.ToastUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_forget_pass.*
import kotlinx.android.synthetic.main.activity_forget_pass.et_password
import kotlinx.android.synthetic.main.activity_forget_pass.et_user
import kotlinx.android.synthetic.main.activity_forget_pass.iv_back
import kotlinx.android.synthetic.main.activity_forget_pass.progress
import kotlinx.android.synthetic.main.activity_login.*

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
                        tv_send_confirm.isEnabled=true
                    }

                    override fun onTick(millisUntilFinished: Long) {
                        tv_send_confirm.text=(millisUntilFinished/1000).toInt().toString()+"s"
                    }

                }.start()
                //发送验证码
                sendCode();
            }
        }

        iv_back.setOnClickListener {
            finish()
        }

        btn_sumbit.setOnClickListener {
            if (progress.visibility == View.VISIBLE){
                return@setOnClickListener
            }
            anchorResetPwd()
        }
    }

    //发送短信验证码
    fun sendCode(){
        APIFactory.create().anchorCode(et_user.text.toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe (
                        { it: SendCodeRsp ->
                            if (it.code==0){
                                ToastUtils.showLong("已发送短信!");
                            }else{
                                ToastUtils.showLong(it?.message?:"");
                            }
                        },
                        {
                            ToastUtils.showLong("网络异常");
                        }
                        ,{})
    }


    //发送短信验证码
    fun anchorResetPwd(){
        APIFactory.create().anchorResetPwd(et_user.text?.toString()?:"",et_password.text?.toString()?:"",et_code.text?.toString()?:"")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe (
                        { it: SendCodeRsp ->
                            if (it.code==0){
                                ToastUtils.showLong("修改成功！");
                                finish()
                            }else{
                                ToastUtils.showLong(it?.message?:"");
                            }
                        },
                        {
                            progress.visibility = View.GONE
                            ToastUtils.showLong("网络异常");
                        }
                        ,{progress.visibility = View.GONE})
    }
}