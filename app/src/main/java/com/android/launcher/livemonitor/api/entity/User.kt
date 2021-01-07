package com.android.launcher.livemonitor.api.entity

import java.io.Serializable

data class User(
        var code: Int,
        var result: UserData?,
        var message: Any
):Serializable {
    data class UserData(
            var token_type: String,
            var access_token: String,
            var id: Int,
            var name: String
    )
}