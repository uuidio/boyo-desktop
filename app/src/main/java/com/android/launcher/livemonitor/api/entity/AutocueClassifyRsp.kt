package com.android.launcher.livemonitor.api.entity

data class AutocueClassifyRsp(
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
            var from: Int,
            var last_page: Int,
            var last_page_url: String,
            var next_page_url: Any,
            var path: String,
            var per_page: String,
            var prev_page_url: Any,
            var to: Int,
            var total: Int
    )

    data class Data(
            var classify_name: String,
            var count: Int,
            var created_at: String,
            var id: Int,
            var live_id: Int,
            var updated_at: String
    )
}