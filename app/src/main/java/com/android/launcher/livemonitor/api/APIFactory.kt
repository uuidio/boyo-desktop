package com.android.launcher.livemonitor.api


/**
 * @author Ben
 * @date 2019/10/9
 */
object APIFactory {
    fun create(): APIService {
        return NaoManager.create(APIService::class.java)
    }
}