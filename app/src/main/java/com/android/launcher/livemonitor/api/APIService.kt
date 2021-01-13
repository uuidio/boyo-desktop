package com.android.launcher.livemonitor.api

import com.android.launcher.livemonitor.api.entity.*
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

    //素材分类列表
    @GET("/live/v1/tag/list")
    fun tagList(@HeaderMap headerMap:HashMap<String,String>,@Query("per_page") per_page:Int): Observable<TagListRsp>


    //素材图片列表
    @GET("/live/v1/tag/image/list")
    fun imageList(@HeaderMap headerMap:HashMap<String,String>,@Query("tag_id") tag_id:Int,@Query("per_page") per_page:Int): Observable<PicImgRsp>

    //个人素材图片列表
    @GET("/live/v1/tag/image/list?select=1")
    fun imagePeopleList(@HeaderMap headerMap:HashMap<String,String>,@Query("per_page") per_page:Int): Observable<PicImgRsp>

    //个人素材图片保存或删去操作(0：删除，1：新增）
    @FormUrlEncoded
    @POST("/live/v1/tagImageApp/save")
    fun imagePeopleSave(@HeaderMap headerMap:HashMap<String,String>, @Field("img_id") img_id:Int, @Field("select") select:Int, @Field("location") location:String): Observable<PicImgRsp>


    //题词分类列表
    @GET("/live/v1/autocue/classify/list")
    fun autocueClassifyList(@HeaderMap headerMap:HashMap<String,String>,@Query("per_page") per_page:Int): Observable<AutocueClassifyRsp>


    //题词列表
    @GET("/live/v1/autocue/list")
    fun autocueList(@HeaderMap headerMap:HashMap<String,String>,@Query("cid") cid:Int,@Query("per_page") per_page:Int): Observable<AutocueRsp>


}

