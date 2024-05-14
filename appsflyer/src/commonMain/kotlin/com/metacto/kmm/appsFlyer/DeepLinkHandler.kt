package com.metacto.kmm.appsFlyer

interface DeepLinkHandler {
    fun initialize()
    fun start()
    fun setCustomerUserId(userId: String)
}

class AppsFlyerDeepLinkHandler(
    private val oneLinkService: OneLinkService
) : DeepLinkHandler {
    override fun initialize() {
        oneLinkService.initialize()
    }

    override fun start() {
        oneLinkService.start()
    }

    override fun setCustomerUserId(userId: String) {
        oneLinkService.setCustomerUserId(userId)
    }
}