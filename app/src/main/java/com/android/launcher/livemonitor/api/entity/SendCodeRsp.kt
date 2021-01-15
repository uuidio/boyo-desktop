package com.android.launcher.livemonitor.api.entity

data class SendCodeRsp(
    val code: Int,
    val message: String,
    val result: List<Any>
){

}