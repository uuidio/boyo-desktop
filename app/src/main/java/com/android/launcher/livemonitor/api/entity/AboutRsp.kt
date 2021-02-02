package com.android.launcher.livemonitor.api.entity

data class AboutRsp(
    var code: Int,
    var message: String,
    var result: Result?
){
    data class Result(
            var created_at: String,
            var id: Int,
            var notice: String,
            var title: String,
            var img: String,
            var location: String,
            var wide_ratio: String,
            var updated_at: String
    )
}