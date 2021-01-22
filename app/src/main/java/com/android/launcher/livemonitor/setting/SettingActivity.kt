package com.android.launcher.livemonitor.setting

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.android.launcher.GsonUtil
import com.android.launcher.R
import com.android.launcher.livemonitor.api.APIFactory
import com.android.launcher.livemonitor.api.NaoManager
import com.android.launcher.livemonitor.api.entity.VersionRsp
import com.android.launcher.livemonitor.common.ToastUtils
import com.android.launcher.livemonitor.dialog.RemindDialog
import com.orhanobut.logger.Logger
import com.sm.http.download.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_setting.*
import sm.utils.AppUtils
import sm.utils.FileUtils
import java.io.File

class SettingActivity : AppCompatActivity() {

    val upDialog by lazy { RemindDialog(this, "检查到有新版本，是否马上更新？") }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        ll_back.setOnClickListener { finish() }
        ll_network.setOnClickListener { startActivity(Intent(this, NetWorkSettingActivity::class.java)) }
        ll_updata.setOnClickListener {
            APIFactory.create().checkVersion(AppUtils.getAppVersionName())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        if (it.code==0) {
                            upDialog.show()
                            upDialog.clickLister = (object : RemindDialog.OnClickLister {
                                override fun cancle() {
                                    upDialog.dismiss()
                                    return
                                }

                                override fun ok() {
                                    startDownload(it)
                                }
                            })
                        } else {
                            ToastUtils.showLong("已经是最新版本")
                        }
                    }, {
                        ToastUtils.showLong("检查版本失败:" + it.message)
                    }, {

                    })

        }


        ll_locat.setOnClickListener { startActivity(Intent(this, AboutNativeActivity::class.java)) }
        ll_about.setOnClickListener { startActivity(Intent(this, AboutUsActivity::class.java)) }
        tv_clean_add.visibility = View.GONE
        tv_clean_add.setOnClickListener { }
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
//            installApkWithMd5(fileName, md5)
            startActivity(getInstallAppIntent(File(fileName)))
        }

        override fun onTaskFailure(task: DownloadTask, e: DownloadException) {
            upDialog.dismiss()
            toastLong("APK下载失败:$e")
        }
    }

    private fun installApkWithMd5(apkPath: String, apkMd5: String) {
        val md5 = FileUtils.getFileMD5ToString(File(apkPath))
        val success = md5?.toUpperCase() == apkMd5.toUpperCase()
        if (success) {
            startActivity(getInstallAppIntent(File(apkPath)))
        } else {
            Logger.e("md5 check fail: s:$md5,l:$apkMd5")
            ToastUtils.showShort("文件检查失败")
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
            val apkUrl =data.result.asJsonObject.get("url").toString()
            val filePath = this.cacheDir!!.path + File.separator + "apk/live_monitor.apk"
            downloadId = DownloadTaskBuilder()
                    .setFileUrl(apkUrl)
                    .setFileName(filePath)
                    .setRetryTime(5)
                    .setConcurrency(1)
                    .setRetryInterval(10000)
                    .setDownloadListener(DownloadApkListener())
                    .build()
                    .run { Downloader.getInstance().start(this) }
        }
    }

    companion object {
    }
}