package com.android.launcher.livemonitor.api

import android.annotation.SuppressLint
import android.content.Context
import com.android.launcher.GsonUtil
import com.android.launcher.LiveApplication
import com.android.launcher.SharedPreferencesUtils
import com.android.launcher.livemonitor.api.entity.User
import com.android.launcher.livemonitor.common.BuildType
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

@SuppressLint("StaticFieldLeak")
/**
 * Created on 2020/12/11.
 *
 * @author stone
 */

object NaoManager {

    private lateinit var buildType: String
    private val retrofit: Retrofit by lazy { createRetrofit() }
    private lateinit var context: Context

    fun init(context: Context, buildType: String) {
        this.context = context.applicationContext
        NaoManager.buildType = buildType
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
                //.baseUrl(baseUrl(buildType))
                .baseUrl(baseUrl(buildType))
                .client(createHttpClient(buildType))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    private fun createHttpClient(buildType: String): OkHttpClient {
        return OkHttpClient().newBuilder()
                .sslSocketFactory(createSSLSocketFactory(), TrustAllCerts())
                .hostnameVerifier(TrustAllHostnameVerifier())
                .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
                .addInterceptor(TimeoutInterceptor(20)).build()
    }

    private fun createSSLSocketFactory(): SSLSocketFactory? {
        var ssfFactory: SSLSocketFactory? = null
        try {
            val sc = SSLContext.getInstance("TLS")
            sc.init(null as Array<KeyManager?>?, arrayOf<TrustManager>(TrustAllCerts()), SecureRandom())
            ssfFactory = sc.socketFactory
        } catch (var2: Exception) {
        }
        return ssfFactory
    }

    private class TrustAllHostnameVerifier constructor() : HostnameVerifier {
        override fun verify(hostname: String, session: SSLSession): Boolean {
            return true
        }
    }

    private class TrustAllCerts constructor() : X509TrustManager {
        @Throws(CertificateException::class)
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        @Throws(CertificateException::class)
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }
    }

    class TimeoutInterceptor internal constructor(private val defaultTimeout: Int) : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            var realTimeout = 0
            val timeout = request.header("Timeout")

            realTimeout = try {
                Integer.parseInt(timeout ?: "")
            } catch (e: NumberFormatException) {
                this.defaultTimeout
            }

            return chain.withConnectTimeout(realTimeout, TimeUnit.SECONDS).withReadTimeout(realTimeout, TimeUnit.SECONDS).proceed(request)
        }
    }

    fun baseUrl(buildType: String = this.buildType): String {
        return "https://api-dev.boyo.tv/"
//        return when (buildType) {
//            BuildType.DEBUG, BuildType.PREVIEW -> "http://testv20.smartconns.com:8095/"
//            else -> "https://apigw.jizhigou.smartconns.com:9998/"
//        }
    }

    fun <T> create(apiClass: Class<out T>): T {
        return retrofit.create(apiClass)
    }

    fun getAccessToken():HashMap<String,String>{
        val userData = GsonUtil.gsonToBean(SharedPreferencesUtils.getParam(LiveApplication.liveApplication, "userData", "").toString(), User.UserData::class.java)
        var headerMap=HashMap<String,String>();
//        headerMap["Accept"] = "application/json"
        headerMap["Authorization"] =(userData?.token_type?:"")+" "+(userData?.access_token?:"")
        return headerMap;
    }


    fun getUploadAccessToken():HashMap<String,String>{
        val userData = GsonUtil.gsonToBean(SharedPreferencesUtils.getParam(LiveApplication.liveApplication, "userData", "").toString(), User.UserData::class.java)
        var headerMap=HashMap<String,String>();
        headerMap["content-type"]="multipart/form-data"
        headerMap["Authorization"] =(userData?.token_type?:"")+" "+(userData?.access_token?:"")
        return headerMap;
    }



}
