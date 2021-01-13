package com.android.launcher

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.android.launcher.livemonitor.api.APIFactory
import com.android.launcher.livemonitor.api.entity.User
import com.android.launcher.livemonitor.common.ToastUtils
import com.android.launcher.livemonitor.setting.SettingActivity
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        tv_forget_password.setOnClickListener {
            startActivity(Intent(this@LoginActivity,ForgetPassActivity::class.java))
        }

        btn_login.setOnClickListener {
            progress.visibility= View.VISIBLE
            login()
        }

        iv_setting.setOnClickListener {
            startActivity(Intent(this@LoginActivity,SettingActivity::class.java))

        }

        iv_back.setOnClickListener {
            finish()
        }

        //判断是否有登录信息
        var userData=SharedPreferencesUtils.getParam(this@LoginActivity,"userData","").toString()
        if (userData.isNotEmpty()){
            startActivity(Intent(this@LoginActivity,MainActivity::class.java))
        }
    }

    //denglu
    private fun login() {
        var map=HashMap<String,String>()
        map.put("username",et_user.text.toString())
        map.put("password",et_password.text.toString())
        map.put("username","18589023060")
        map.put("password","qwe123")
        APIFactory.create().login(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.code==0){
                        SharedPreferencesUtils.setParam(this@LoginActivity,"userData",GsonUtil.gsonString(it.result));
                        finish()
                        startActivity(Intent(this@LoginActivity,MainActivity::class.java))

                    }
                }, {
                    progress.visibility= View.GONE
                    ToastUtils.showLong("账号密码错误或没有网络");
                }, {
                    progress.visibility= View.GONE
                })
    }
}