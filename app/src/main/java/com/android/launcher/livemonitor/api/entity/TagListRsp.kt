package com.android.launcher.livemonitor.api.entity

import java.io.Serializable

data class TagListRsp(
    var code: Int,
    var message: String,
    var result: Result
):Serializable {
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
            var next_page_url: String,
            var path: String,
            var per_page: Int,
            var prev_page_url: Any,
            var to: Int,
            var total: Int
    )

    data class Data(
            var created_at: String,
            var id: Int,
            var live_id: Int,
            var name: String,
            var updated_at: String
    )
}