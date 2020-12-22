package com.android.launcher.livemonitor.api

import com.android.launcher.livemonitor.api.entity.VersionRsp
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


/**
 * @author stone
 * @date 2020/12/11
 */
interface APIService {

    @GET("/apks/check/{version}/")
    fun checkVersion(@Path("version") version: Int, @Query("type") type: String = "LIVE"): Observable<VersionRsp>
}

