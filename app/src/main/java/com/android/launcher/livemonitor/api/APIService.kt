package com.android.launcher.livemonitor.api

import com.android.launcher.livemonitor.api.entity.*
import com.google.gson.JsonElement
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import java.io.File


/**
 * @author stone
 * @date 2020/12/11
 */
interface APIService {

    @GET("/live/v1/versions/check")
    fun checkVersion(@Query("versions") versions: String): Observable<VersionRsp>


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
    @GET("/live/v1/liveTag/image/list")
    fun imagePeopleList(@HeaderMap headerMap:HashMap<String,String>,@Query("per_page") per_page:Int): Observable<PeoPleImgRsp>

    //个人素材图片上传
    @Multipart
    @POST("/live/v1/upload/image")
    fun uploadImage(@HeaderMap headerMap:HashMap<String,String>, @Part image:MultipartBody.Part): Observable<JsonElement>


    //个人素材图片保存或删去操作(0：删除，1：新增，2：修改）
    @FormUrlEncoded
    @POST("/live/v1/tagImageApp/save")
    fun imagePeopleSave(@HeaderMap headerMap:HashMap<String,String>, @Field("img_id") img_id:Int, @Field("select") select:Int, @Field("location") location:String
                        ,@Field("img") imagePath:String,@Field("id") id:Int): Observable<NormalRsp>


    //题词分类列表
    @GET("/live/v1/autocue/classify/list")
    fun autocueClassifyList(@HeaderMap headerMap:HashMap<String,String>,@Query("per_page") per_page:Int): Observable<AutocueClassifyRsp>


    //题词列表
    @GET("/live/v1/autocue/list")
    fun autocueList(@HeaderMap headerMap:HashMap<String,String>,@Query("cid") cid:Int,@Query("per_page") per_page:Int): Observable<AutocueRsp>

    //公告
    @GET("/live/v1/notice/get")
    fun notice(@HeaderMap headerMap:HashMap<String,String>): Observable<AboutRsp>


    //发送手机验证码
    @FormUrlEncoded
    @POST("/live/v1/anchor/code")
    fun anchorCode(@Field("mobile") mobile:String): Observable<SendCodeRsp>

    //验证码重置密码
    @FormUrlEncoded
    @POST("/live/v1/anchor/resetPwd")
    fun anchorResetPwd(@Field("mobile") mobile:String,@Field("password") password:String,@Field("code") code:String): Observable<SendCodeRsp>


    //验证登录TOKEN
    @GET("/live/v1/live/oauth")
    fun liveOauth(@HeaderMap headerMap:HashMap<String,String>): Observable<NormalRsp>


    //检查TOKEN
    @GET("/live/v1/notice/get")
    fun checkNotice(@HeaderMap headerMap:HashMap<String,String>): Observable<NormalRsp>
}

