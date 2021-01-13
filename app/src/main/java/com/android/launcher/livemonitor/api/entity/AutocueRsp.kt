package com.android.launcher.livemonitor.api.entity

data class AutocueRsp(
    val code: Int,
    val message: String,
    val result: Result
) {

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
            val antistop_one: String,
            val antistop_three: String,
            val antistop_two: String,
            val cid: Int,
            val content: String,
            val created_at: String,
            val id: Int,
            val live_id: Int,
            val sort: Int,
            val title: String,
            val updated_at: String
    )
}