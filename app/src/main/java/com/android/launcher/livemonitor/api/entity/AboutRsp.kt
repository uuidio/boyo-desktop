package com.android.launcher.livemonitor.api.entity

data class AboutRsp(
    val code: Int,
    val message: String,
    val result: Result
){
    data class Result(
            val created_at: String,
            val id: Int,
            val notice: String,
            val title: String,
            val updated_at: String
    )
}