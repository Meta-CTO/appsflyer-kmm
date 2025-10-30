package com.metacto.kmm.appsflyer.model

data class DeepLinkResult(
    val destination: String?,
    val campaign: String?,
    val campaignId: String?,
    val clickHttpReferrer: String?,
    val isDeferred: Boolean?,
    val mediaSource: String?,
    val matchType: String?,
    val clickEventJson: String?,
    val metadata: DeepLinkMetadata?,
    val deeplinkSource: DeeplinkSource,
    val hasDescopeToken: Boolean,
    val hasLoginType: Boolean,
    val loginType: LoginType?,
    val udlStatus: UdlStatus?,
    val udlMatchType: String?,
    val gcdAfStatus: GcdAfStatus?,
    val gcdMediaSource: String?,
    val gcdCampaign: String?,
    val afSub1: String?,
    val extraLink: String?
)