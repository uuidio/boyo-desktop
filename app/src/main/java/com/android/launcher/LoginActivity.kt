package com.android.launcher

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.ProgressBar
import androidx.core.content.FileProvider
import com.android.launcher.livemonitor.api.APIFactory
import com.android.launcher.livemonitor.api.NaoManager
import com.android.launcher.livemonitor.api.entity.User
import com.android.launcher.livemonitor.api.entity.VersionRsp
import com.android.launcher.livemonitor.common.ToastUtils
import com.android.launcher.livemonitor.dialog.RemindDialog
import com.android.launcher.livemonitor.setting.SettingActivity
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import com.sm.http.download.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import sm.utils.AppUtils
import sm.utils.FileUtils
import java.io.File

class LoginActivity : AppCompatActivity() {

    val upDialog by lazy { RemindDialog(this, "检查到有新版本，是否马上更新？") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        tv_forget_password.setOnClickListener {
            startActivity(Intent(this@LoginActivity,ForgetPassActivity::class.java))
        }

        btn_login.setOnClickListener {
            if (progress.visibility == View.VISIBLE){
                return@setOnClickListener
            }

            checkUpdate()
        }

        iv_setting.setOnClickListener {
            startActivity(Intent(this@LoginActivity,SettingActivity::class.java))

        }

        iv_back.setOnClickListener {
            finish()
        }

        var loginUser=SharedPreferencesUtils.getParam(this@LoginActivity,"loginUser","");
        var loginPass=SharedPreferencesUtils.getParam(this@LoginActivity,"loginPass","");
        et_user.setText(loginUser.toString())
        et_password.setText(loginPass.toString())

        //检查更新
        checkUpdate()

    }

    //denglu
    private fun login() {
        progress.visibility= View.VISIBLE
        if (et_user.text.toString().isEmpty() || et_password.text.toString().isEmpty()){
            progress.visibility= View.GONE
            ToastUtils.showLong("账号或密码没有填写")
            return
        }
        var map=HashMap<String,String>()
        map.put("username",et_user.text.toString())
        map.put("password",et_password.text.toString())
//        map.put("username","18589023060")
//        map.put("password","qwe123")
        APIFactory.create().login(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    progress.visibility= View.GONE
                    if (it.code==0){
                        SharedPreferencesUtils.setParam(this@LoginActivity,"userData",GsonUtil.gsonString(it.result));
                        SharedPreferencesUtils.setParam(this@LoginActivity,"loginUser",map.get("username").toString());
                        SharedPreferencesUtils.setParam(this@LoginActivity,"loginPass",map.get("password").toString());
                        liveOauth()
                    }else{
                        progress.visibility= View.GONE
                        ToastUtils.showLong(it.message);
                    }
                }, {
                    progress.visibility= View.GONE
                    ToastUtils.showLong("网络异常");
                }, {

                })
    }


    //验证登录token
    private fun liveOauth() {
        APIFactory.create().liveOauth(NaoManager.getAccessToken())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.code==0){
                        finish()
                        startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                    }else{
                        ToastUtils.showLong(it.message);
                    }
                }, {
                    progress.visibility= View.GONE
                    ToastUtils.showLong("网络异常!");
                }, {
                    progress.visibility= View.GONE
                })
    }


    //检查更新APK
    @SuppressLint("CheckResult")
    private fun checkUpdate() {
        if (upDialog.isShowing){
            return
        }
        progress.visibility= View.VISIBLE
        APIFactory.create().checkVersion(AppUtils.getAppVersionName())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    progress.visibility= View.GONE
                    if (upDialog.isShowing){
                        return@subscribe
                    }
                    if (it.code==0){
                            upDialog.show()
                            upDialog.setTitle("检查到有新版本")
                            upDialog.clickLister = null
                            startDownload(it)
                    }else{
                        //判断是否有登录信息
                        var userData=SharedPreferencesUtils.getParam(this@LoginActivity,"userData","").toString()
                        if (userData.isNotEmpty()){
                           login()
                        }
                    }
                }, {
                    progress.visibility= View.GONE
                    if (upDialog.isShowing){
                        return@subscribe
                    }
                    ToastUtils.showLong("网络异常!");
                    upDialog.show()
                    upDialog.setTitle("网络异常，请重新点击下载")
                    upDialog.clickLister = (object : RemindDialog.OnClickLister {
                        override fun cancle() {
                            upDialog.dismiss()
                            return
                        }

                        override fun ok() {
                            upDialog.dismiss()
                            checkUpdate()

                        }
                    })
                }, {
                    progress.visibility= View.GONE
                })
    }

    private var downloadId = 0
    private inner class DownloadApkListener() : DownloadListener {

        private fun toastLong(message: String) {
            ToastUtils.showLong(message)
        }

        override fun onTaskCancel(task: DownloadTask?) {
            toastLong("APK下载已取消")
            upDialog.dismiss()
        }

        override fun onTaskStart() {
            toastLong("APK下载开始")
            upDialog.startUp()
        }

        override fun onTaskProgress(soFarBytes: Long, totalBytes: Long) {
            toastLong("APK下载中:总大小:${(totalBytes / 1024 / 1024).run {
                if (this == 0L) {
                    "未知"
                } else {
                    "${this}M"
                }
            }} 已下载:${soFarBytes / 1024}K")
            upDialog.setProgress((soFarBytes * 100 / totalBytes).toInt())
        }

        override fun onTaskSuccess(fileName: String) {
            toastLong("APK下载完成")
            upDialog.dismiss()
            startActivity(getInstallAppIntent(File(fileName)))
        }

        override fun onTaskFailure(task: DownloadTask, e: DownloadException) {
            upDialog.dismiss()
            toastLong("APK下载失败:$e")
        }
    }


    private fun getInstallAppIntent(file: File): Intent {
        val intent = Intent("android.intent.action.VIEW")
        val type: String
        if (Build.VERSION.SDK_INT < 23) {
            intent.action = "android.intent.action.VIEW"
            type = "application/vnd.android.package-archive"
            intent.setDataAndType(Uri.fromFile(file), type)
        } else {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(FileUtils.getFileExtension(file))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            val contentUri = FileProvider.getUriForFile(this, "com.android.launcher.ApkFileProvider", file)
            intent.setDataAndType(contentUri, type)
        }
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    private fun startDownload(data: VersionRsp) {
        if (data.result!=null && data.result.isJsonObject && data.result.asJsonObject.has("url")) {
            var apkUrl =data.result.asJsonObject.get("url").asString
            var filePath = this.cacheDir!!.path + File.separator + "apk/live_monitor.apk"
              downloadId= DownloadTaskBuilder()
                    .setFileUrl(apkUrl)
                    .setFileName(filePath)
                    .setRetryTime(5)
                    .setConcurrency(1)
                    .setRetryInterval(10000)
                    .setDownloadListener(DownloadApkListener())
                    .build()
                     .run {Downloader.getInstance().start(this)  }



        }
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            return  true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onBackPressed() {

    }
}