package com.metacto.kmm.appsflyer

interface DeepLinkHandler {
    fun initialize()
    fun start()
    fun setCustomerUserId(userId: String)
}

class AppsFlyerDeepLinkHandler(
    private val appsFlyerOneLinkService: AppsFlyerOneLinkService
) : DeepLinkHandler {
    override fun initialize() {
        appsFlyerOneLinkService.initialize()
    }

    override fun start() {
        appsFlyerOneLinkService.start()
    }

    override fun setCustomerUserId(userId: String) {
        appsFlyerOneLinkService.setCustomerUserId(userId)
    }
}