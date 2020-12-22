package com.android.launcher.livemonitor.api.entity

data class VersionRsp(
        val code: Int,
        val `data`: VersionData?,
        val msg: Any,
        val ok: Boolean
){
    data class VersionData(
            val createTime: String,
            val description: String,
            val fileKey: String,
            val id: Int,
            val md5: String,
            val name: String,
            val seq: Int,
            val status: String,
            val type: String
    )
}