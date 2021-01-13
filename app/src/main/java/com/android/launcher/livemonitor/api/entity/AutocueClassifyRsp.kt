package com.android.launcher.livemonitor.api.entity

data class AutocueClassifyRsp(
    val code: Int,
    val message: String,
    val result: Result
){
    data class Result(
            val lists: Lists
    )

    data class Lists(
            val current_page: Int,
            val `data`: List<Data>,
            val first_page_url: String,
            val from: Int,
            val last_page: Int,
            val last_page_url: String,
            val next_page_url: Any,
            val path: String,
            val per_page: String,
            val prev_page_url: Any,
            val to: Int,
            val total: Int
    )

    data class Data(
            val classify_name: String,
            val count: Int,
            val created_at: String,
            val id: Int,
            val live_id: Int,
            val updated_at: String
    )
}