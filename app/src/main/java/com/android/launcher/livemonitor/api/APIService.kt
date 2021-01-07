package com.android.launcher.livemonitor.api

import com.android.launcher.livemonitor.api.entity.User
import com.android.launcher.livemonitor.api.entity.VersionRsp
import com.google.gson.JsonElement
import io.reactivex.Observable
import retrofit2.http.*


/**
 * @author stone
 * @date 2020/12/11
 */
interface APIService {

    @GET("/apks/check/{version}/")
    fun checkVersion(@Path("version") version: Int, @Query("type") type: String = "LIVE"): Observable<VersionRsp>


    @FormUrlEncoded
    @POST("/live/v1/anchor/login")
    fun login(@FieldMap hash:HashMap<String,String>): Observable<User>

}

