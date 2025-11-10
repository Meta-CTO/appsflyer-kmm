@file:OptIn(ExperimentalForeignApi::class)

package com.metacto.kmm.appsflyer

import AppsFlyerLib.AFSDKDeepLinkResultStatus
import AppsFlyerLib.AppsFlyerDeepLinkDelegateProtocol
import AppsFlyerLib.AppsFlyerDeepLinkResult
import AppsFlyerLib.AppsFlyerLib
import AppsFlyerLib.AppsFlyerLibDelegateProtocol
import com.metacto.kmm.appsflyer.model.*
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
        println("🔗 DEEPLINK [KMM-UDL] didResolveDeepLink called with status: ${result.status}")
        when (result.status) {
            AFSDKDeepLinkResultStatus.AFSDKDeepLinkResultStatusFound -> {
                println("🔗 DEEPLINK [KMM-UDL] Status: FOUND")
                val deepLink = result.deepLink
                val clickEventValues = deepLink?.clickEvent?.toMap() ?: emptyMap()
                println("🔗 DEEPLINK [KMM-UDL] clickEventValues: $clickEventValues")

                val deepLinkValue = deepLink?.deeplinkValue ?: clickEventValues.getDeepLinkValue()
                println("🔗 DEEPLINK [KMM-UDL] deepLinkValue: $deepLinkValue")

                val destination = deepLinkValue?.parseDestination()
                println("🔗 DEEPLINK [KMM-UDL] destination: $destination")

                val deepLinkResult = DeepLinkResult(
                    origin = clickEventValues.buildOrigin(DeeplinkSource.UDL, UdlStatus.FOUND, null, deepLink?.isDeferred),
                    campaign = clickEventValues.buildCampaign(),
                    advertisement = clickEventValues.buildAdvertisement(),
                    deepLink = clickEventValues.buildDeepLink(destination),
                    timestamps = clickEventValues.buildTimestamps(),
                    cost = clickEventValues.buildCost(),
                    device = clickEventValues.buildDevice(),
                    retargeting = clickEventValues.buildRetargeting(),
                    engagement = clickEventValues.buildEngagement(),
                    viewability = clickEventValues.buildViewability(),
                    network = clickEventValues.buildNetwork(),
                    customParameters = clickEventValues.buildCustomParameters(),
                    socialPreview = clickEventValues.buildSocialPreview(),
                    system = clickEventValues.buildSystem(),
                    clickEvent = clickEventValues,
                    conversion = null
                )

                println("🔗 DEEPLINK [KMM-UDL] ===== DeepLinkResult =====")
                println("🔗 DEEPLINK [KMM-UDL] $deepLinkResult")
                println("🔗 DEEPLINK [KMM-UDL] =====================================")
                println("🔗 DEEPLINK [KMM-UDL] Calling listener.onDeepLinkingResult")
                options.listener.onDeepLinkingResult(deepLinkResult)
            }

            AFSDKDeepLinkResultStatus.AFSDKDeepLinkResultStatusNotFound -> {
                println("🔗 DEEPLINK [KMM-UDL] Status: NOT_FOUND")
                options.listener.onDeepLinkingResult(null)
            }

            AFSDKDeepLinkResultStatus.AFSDKDeepLinkResultStatusFailure -> {
                println("🔗 DEEPLINK [KMM-UDL] Status: FAILURE - error: ${result.error}")
                options.listener.onDeepLinkingError(DeepLinkError(result.error))
            }

            else -> {
                println("🔗 DEEPLINK [KMM-UDL] Status: UNKNOWN")
                options.listener.onDeepLinkingResult(null)
            }
        }
    }

    override fun onConversionDataFail(error: NSError) {
        println("🔗 DEEPLINK [KMM-GCD] onConversionDataFail: ${error.localizedDescription}")
    }

    override fun onConversionDataSuccess(conversionInfo: Map<Any?, *>) {
        println("🔗 DEEPLINK [KMM-GCD] onConversionDataSuccess called")
        println("🔗 DEEPLINK [KMM-GCD] conversionInfo: $conversionInfo")
        println("🔗 DEEPLINK [KMM-GCD] conversionInfo size: ${conversionInfo.size}")
        println("🔗 DEEPLINK [KMM-GCD] conversionInfo keys: ${conversionInfo.keys}")
        println("🔗 DEEPLINK [KMM-GCD] All conversionInfo entries:")
        conversionInfo.forEach { (key, value) ->
            println("🔗 DEEPLINK [KMM-GCD]   $key = $value")
        }

        val appConversionResult = this.getAppAttributionResult(conversionInfo)
        options.listener.onAppAttribution(appConversionResult.isOrganic, appConversionResult.extras)

        val gcdMediaSource = appConversionResult.extras["media_source"]?.toString()
        val gcdCampaign = appConversionResult.extras["campaign"]?.toString()
        @Suppress("UNCHECKED_CAST")
        val extras = appConversionResult.extras as Map<Any?, *>

        println("🔗 DEEPLINK [KMM-GCD] extras: $extras")
        println("🔗 DEEPLINK [KMM-GCD] gcdMediaSource: $gcdMediaSource")
        println("🔗 DEEPLINK [KMM-GCD] gcdCampaign: $gcdCampaign")

        val isFirstLaunch = extras.getBoolean("is_first_launch") ?: false
        println("🔗 DEEPLINK [KMM-GCD] isFirstLaunch: $isFirstLaunch")

        // Try to get deep_link_value from raw conversionInfo first, then from extras
        val deepLinkValue = (conversionInfo["deep_link_value"] ?: extras["deep_link_value"])?.toString()
        println("🔗 DEEPLINK [KMM-GCD] deep_link_value from conversionInfo: ${conversionInfo["deep_link_value"]}")
        println("🔗 DEEPLINK [KMM-GCD] deep_link_value from extras: ${extras["deep_link_value"]}")
        println("🔗 DEEPLINK [KMM-GCD] Final deepLinkValue: $deepLinkValue")

        val destination = deepLinkValue?.parseDestination()
        println("🔗 DEEPLINK [KMM-GCD] destination: $destination")

        val gcdStatus = if (appConversionResult.isOrganic) GcdAfStatus.ORGANIC else GcdAfStatus.NON_ORGANIC

        val deepLinkResult = DeepLinkResult(
            origin = extras.buildOrigin(DeeplinkSource.GCD, null, gcdStatus, isFirstLaunch),
            campaign = extras.buildCampaign(),
            advertisement = extras.buildAdvertisement(),
            deepLink = extras.buildDeepLink(destination),
            timestamps = extras.buildTimestamps(),
            cost = extras.buildCost(),
            device = extras.buildDevice(),
            retargeting = extras.buildRetargeting(),
            engagement = extras.buildEngagement(),
            viewability = extras.buildViewability(),
            network = extras.buildNetwork(),
            customParameters = extras.buildCustomParameters(),
            socialPreview = extras.buildSocialPreview(),
            system = extras.buildSystem(),
            clickEvent = null,
            conversion = conversionInfo
        )

        println("🔗 DEEPLINK [KMM-GCD] ===== DeepLinkResult =====")
        println("🔗 DEEPLINK [KMM-GCD] $deepLinkResult")
        println("🔗 DEEPLINK [KMM-GCD] =====================================")
        println("🔗 DEEPLINK [KMM-GCD] Calling listener.onDeepLinkingResult")
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