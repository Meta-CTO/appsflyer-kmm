package com.metacto.kmm.appsflyer

expect class OneLinkService(
    options: OneLinkOptions
) {
    fun initialize()

    fun start(onSuccess: () -> Unit, onError: (Throwable) -> Unit)
    fun start()
    fun stop(isStopped: Boolean)

    fun setCustomerUserId(userId: String)
}

class OneLinkOptions(
    val context: Any? = null,
    val appleAppId: String? = null,
    val devAppKey: String,
    val enableDebugLog: Boolean? = null,
    val minTimeBetweenSessions: Int? = null,
    val appInviteOneLinkTemplateId: String? = null,
    val listener: OneLinkListener,
    val oneLinkCustomDomains: List<String>? = null
)