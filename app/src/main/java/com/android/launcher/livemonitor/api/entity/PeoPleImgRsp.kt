package com.android.launcher.livemonitor.api.entity
//个人贴纸
data class PeoPleImgRsp(
    var code: Int,
    var message: String,
    var result: Result
){
    data class Result(
            var lists: Lists
    )
    data class Lists(
            var current_page: Int,
            var `data`: List<Data>,
            var first_page_url: String,
            var from: Any,
            var last_page: Int,
            var last_page_url: String,
            var next_page_url: Any,
            var path: String,
            var per_page: Int,
            var prev_page_url: Any,
            var to: Any,
            var total: Int
    )

    data class Data(
            var id: Int,
            var img_id: Int,
            var name: String?="",
            var location: String?="",
            var background_img: String?="", //原图
            var img: String?="" //显示图
    )

    data class Location(
            var topPer: Float=0f,
            var leftPer: Float=0f,
            var roate: Float=0f,
            var sizePer: Int=100

    )
}