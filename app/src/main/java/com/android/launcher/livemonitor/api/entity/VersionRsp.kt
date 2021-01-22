package com.android.launcher.livemonitor.api.entity

import com.google.gson.JsonElement

data class VersionRsp(
        var code: Int,
        var result: JsonElement,
        var message: String
){
data class Result(
    var created_at: String,
    var file_name: String,
    var id: Int,
    var updated_at: String,
    var url: String,
    var versions: String
)
    data class VersionData(
            var createTime: String,
            var description: String,
            var fileKey: String,
            var id: Int,
            var md5: String,
            var name: String,
            var seq: Int,
            var status: String,
            var type: String
    )
}