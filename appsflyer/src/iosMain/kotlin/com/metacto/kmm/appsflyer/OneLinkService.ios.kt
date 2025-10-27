@file:OptIn(ExperimentalForeignApi::class)

package com.metacto.kmm.appsflyer

import AppsFlyerLib.AFSDKDeepLinkResultStatus
import AppsFlyerLib.AppsFlyerDeepLinkDelegateProtocol
import AppsFlyerLib.AppsFlyerDeepLinkResult
import AppsFlyerLib.AppsFlyerLib
import AppsFlyerLib.AppsFlyerLibDelegateProtocol
import com.metacto.kmm.appsflyer.model.DeepLinkError
import com.metacto.kmm.appsflyer.model.DeepLinkResult
import com.metacto.kmm.appsflyer.model.getDeepLinkValue
import com.metacto.kmm.appsflyer.model.getDeepLinkMetadata
import com.metacto.kmm.appsflyer.model.getDestinationPath
import com.metacto.kmm.appsflyer.model.hasDescopeToken
import com.metacto.kmm.appsflyer.model.hasLoginType
import com.metacto.kmm.appsflyer.model.getLoginType
import com.metacto.kmm.appsflyer.model.getAfSub1
import com.metacto.kmm.appsflyer.model.DeeplinkSource
import com.metacto.kmm.appsflyer.model.UdlStatus
import com.metacto.kmm.appsflyer.model.GcdAfStatus
import com.metacto.kmm.appsflyer.util.getAppAttributionResult
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.convert
import platform.Foundation.NSError
import platform.darwin.NSObject

actual class OneLinkService actual constructor(
    private val options: OneLinkOptions
) : NSObject(), AppsFlyerDeepLinkDelegateProtocol, AppsFlyerLibDelegateProtocol {
    init {
        if (options.appleAppId.isNullOrEmpty()) {
            throw IllegalArgumentException("Apple App ID must be provided for iOS platform")
        }

        AppsFlyerLib.shared().apply {
            setDelegate(this@OneLinkService)
            setDeepLinkDelegate(this@OneLinkService)
            setOneLinkCustomDomains(options.oneLinkCustomDomains)
            initialize()
        }
    }

    actual fun initialize() {
        AppsFlyerLib.shared().apply {
            options.devAppKey.let {
                setAppsFlyerDevKey(options.devAppKey)
            }

            setAppleAppID(options.appleAppId!!)

            options.enableDebugLog?.let {
                setIsDebug(it)
            }

            options.minTimeBetweenSessions?.let {
                setMinTimeBetweenSessions(it.convert())
            }

            options.appInviteOneLinkTemplateId?.let {
                setAppInviteOneLink(it)
            }

            options.oneLinkCustomDomains?.let {
                setOneLinkCustomDomains(it)
            }
        }
    }

    override fun didResolveDeepLink(result: AppsFlyerDeepLinkResult) {
        when (result.status) {
            AFSDKDeepLinkResultStatus.AFSDKDeepLinkResultStatusFound -> {
                val deepLink = result.deepLink
                val clickEventValues = deepLink?.clickEvent?.toMap() ?: emptyMap()
                val fullDeepLinkValue =
                    deepLink?.deeplinkValue ?: this.getDeepLinkValue(clickEventValues)
                val metadata = this.getDeepLinkMetadata(fullDeepLinkValue, clickEventValues)

                val deepLinkResult = DeepLinkResult(
                    destination = fullDeepLinkValue?.let { this.getDestinationPath(fullDeepLinkValue) },
                    campaign = deepLink?.campaign,
                    campaignId = deepLink?.campaignId,
                    clickHttpReferrer = deepLink?.clickHTTPReferrer,
                    isDeferred = deepLink?.isDeferred,
                    mediaSource = deepLink?.mediaSource,
                    matchType = deepLink?.matchType,
                    clickEventJson = deepLink?.clickEvent.toString(),
                    metadata = metadata,
                    deeplinkSource = DeeplinkSource.UDL,
                    hasDescopeToken = hasDescopeToken(metadata.extras),
                    hasLoginType = hasLoginType(metadata.extras),
                    loginType = getLoginType(metadata.extras),
                    udlStatus = UdlStatus.FOUND,
                    udlMatchType = deepLink?.matchType,
                    gcdAfStatus = null,
                    gcdMediaSource = null,
                    gcdCampaign = null,
                    afSub1 = getAfSub1(clickEventValues)
                )

                options.listener.onDeepLinkingResult(deepLinkResult)
            }

            AFSDKDeepLinkResultStatus.AFSDKDeepLinkResultStatusNotFound -> {
                options.listener.onDeepLinkingResult(null)
            }

            AFSDKDeepLinkResultStatus.AFSDKDeepLinkResultStatusFailure -> {
                options.listener.onDeepLinkingError(DeepLinkError(result.error))
            }

            else -> {
                options.listener.onDeepLinkingResult(null)
            }
        }
    }

    override fun onConversionDataFail(error: NSError) {
        // No-op
    }

    override fun onConversionDataSuccess(conversionInfo: Map<Any?, *>) {
        val appConversionResult = this.getAppAttributionResult(conversionInfo)
        options.listener.onAppAttribution(appConversionResult.isOrganic, appConversionResult.extras)

        val gcdMediaSource = appConversionResult.extras["media_source"]?.toString()
        val gcdCampaign = appConversionResult.extras["campaign"]?.toString()
        @Suppress("UNCHECKED_CAST")
        val extras = appConversionResult.extras as Map<Any?, *>

        val deepLinkResult = DeepLinkResult(
            destination = null,
            campaign = gcdCampaign,
            campaignId = null,
            clickHttpReferrer = null,
            isDeferred = false,
            mediaSource = gcdMediaSource,
            matchType = null,
            clickEventJson = null,
            metadata = null,
            deeplinkSource = DeeplinkSource.GCD,
            hasDescopeToken = hasDescopeToken(extras),
            hasLoginType = hasLoginType(extras),
            loginType = getLoginType(extras),
            udlStatus = null,
            udlMatchType = null,
            gcdAfStatus = if (appConversionResult.isOrganic) GcdAfStatus.ORGANIC else GcdAfStatus.NON_ORGANIC,
            gcdMediaSource = gcdMediaSource,
            gcdCampaign = gcdCampaign,
            afSub1 = getAfSub1(extras)
        )
        options.listener.onDeepLinkingResult(deepLinkResult)
    }

    actual fun start(
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        AppsFlyerLib.shared().startWithCompletionHandler { dictionary, error ->
            if (error != null) {
                onError.invoke(
                    Throwable(error.domain())
                )
            } else {
                onSuccess.invoke()
            }
        }
    }

    actual fun start() {
        AppsFlyerLib.shared().start()
    }

    actual fun stop(isStopped: Boolean) {
        AppsFlyerLib.shared().setIsStopped(isStopped)
    }

    actual fun setCustomerUserId(userId: String) {
        AppsFlyerLib.shared().setCustomerUserID(userId)
    }
}