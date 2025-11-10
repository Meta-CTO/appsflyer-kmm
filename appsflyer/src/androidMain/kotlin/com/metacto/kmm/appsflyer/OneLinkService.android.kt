package com.metacto.kmm.appsflyer

import android.content.Context
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.appsflyer.attribution.AppsFlyerRequestListener
import com.appsflyer.deeplink.DeepLinkListener
import com.appsflyer.deeplink.DeepLinkResult
import com.metacto.kmm.appsflyer.model.*
import com.metacto.kmm.appsflyer.util.getAppAttributionResult
import com.metacto.kmm.appsflyer.model.DeepLinkResult as KMMDeepLinkResult

actual class OneLinkService actual constructor(
    val options: OneLinkOptions
) {
    init {
        if (options.context == null || options.context !is Context) {
            throw IllegalArgumentException("Context must be provided and must be an instance of android.content.Context")
        }
    }


    private val conversionListener = object : AppsFlyerConversionListener {
        override fun onConversionDataSuccess(p0: MutableMap<String, Any>?) {
            if (p0 != null) {
                println("🔗 DEEPLINK [KMM-GCD] onConversionDataSuccess called")
                println("🔗 DEEPLINK [KMM-GCD] conversionInfo: $p0")

                val appConversionResult = this@OneLinkService.getAppAttributionResult(p0)
                options.listener.onAppAttribution(
                    appConversionResult.isOrganic,
                    appConversionResult.extras
                )

                val gcdMediaSource = appConversionResult.extras["media_source"]?.toString()
                val gcdCampaign = appConversionResult.extras["campaign"]?.toString()
                @Suppress("UNCHECKED_CAST")
                val extras = appConversionResult.extras as Map<Any?, *>

                println("🔗 DEEPLINK [KMM-GCD] extras: $extras")
                println("🔗 DEEPLINK [KMM-GCD] gcdMediaSource: $gcdMediaSource")
                println("🔗 DEEPLINK [KMM-GCD] gcdCampaign: $gcdCampaign")

                val isFirstLaunch = (extras["is_first_launch"] as? String)?.toBoolean()
                    ?: (extras["is_first_launch"] as? Boolean)
                    ?: false
                println("🔗 DEEPLINK [KMM-GCD] isFirstLaunch: $isFirstLaunch")

                val deepLinkValue = extras.getDeepLinkValue()
                println("🔗 DEEPLINK [KMM-GCD] deepLinkValue: $deepLinkValue")

                val destination = deepLinkValue?.parseDestination()
                println("🔗 DEEPLINK [KMM-GCD] destination: $destination")

                val result = KMMDeepLinkResult(
                    origin = extras.buildOrigin(
                        source = DeeplinkSource.GCD,
                        udlStatus = null,
                        gcdStatus = if (appConversionResult.isOrganic) GcdAfStatus.ORGANIC else GcdAfStatus.NON_ORGANIC,
                        isDeferred = isFirstLaunch
                    ),
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
                    clickEvent = extras,
                    conversion = p0 as? Map<Any?, *>
                )

                println("🔗 DEEPLINK [KMM-GCD] ===== DeepLinkResult Summary (onConversionDataSuccess) =====")
                println("🔗 DEEPLINK [KMM-GCD]   origin.source: ${result.origin.source}")
                println("🔗 DEEPLINK [KMM-GCD]   origin.status: ${result.origin.status}")
                println("🔗 DEEPLINK [KMM-GCD]   origin.isDeferred: ${result.origin.isDeferred}")
                println("🔗 DEEPLINK [KMM-GCD]   deepLink.destination: ${result.deepLink.destination}")
                println("🔗 DEEPLINK [KMM-GCD]   deepLink.value: ${result.deepLink.value}")
                println("🔗 DEEPLINK [KMM-GCD]   deepLink.scheme: ${result.deepLink.scheme}")
                println("🔗 DEEPLINK [KMM-GCD]   campaign.mediaSource: ${result.campaign.mediaSource}")
                println("🔗 DEEPLINK [KMM-GCD]   campaign.name: ${result.campaign.name}")
                println("🔗 DEEPLINK [KMM-GCD]   customParameters.sub1: ${result.customParameters.sub1}")
                println("🔗 DEEPLINK [KMM-GCD]   customParameters.sub2: ${result.customParameters.sub2}")
                println("🔗 DEEPLINK [KMM-GCD]   customParameters.sub3: ${result.customParameters.sub3}")
                println("🔗 DEEPLINK [KMM-GCD] =====================================")
                println("🔗 DEEPLINK [KMM-GCD] Calling listener.onDeepLinkingResult with GCD result")
                options.listener.onDeepLinkingResult(result)
            }
        }

        override fun onConversionDataFail(p0: String?) {
            // No-op
        }

        override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {
            if (p0 != null) {
                println("🔗 DEEPLINK [KMM-GCD] onAppOpenAttribution called")
                println("🔗 DEEPLINK [KMM-GCD] attributionData: $p0")

                val appConversionResult = this@OneLinkService.getAppAttributionResult(p0)
                options.listener.onAppAttribution(
                    appConversionResult.isOrganic,
                    appConversionResult.extras
                )

                @Suppress("UNCHECKED_CAST")
                val extras = appConversionResult.extras as Map<Any?, *>

                val deepLinkValue = extras.getDeepLinkValue()
                val destination = deepLinkValue?.parseDestination()

                val result = KMMDeepLinkResult(
                    origin = extras.buildOrigin(
                        source = DeeplinkSource.GCD,
                        udlStatus = null,
                        gcdStatus = if (appConversionResult.isOrganic) GcdAfStatus.ORGANIC else GcdAfStatus.NON_ORGANIC,
                        isDeferred = false
                    ),
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
                    clickEvent = extras,
                    conversion = p0 as? Map<Any?, *>
                )

                println("🔗 DEEPLINK [KMM-GCD] ===== DeepLinkResult =====")
                println("🔗 DEEPLINK [KMM-GCD] $result")
                println("🔗 DEEPLINK [KMM-GCD] =====================================")
                println("🔗 DEEPLINK [KMM-GCD] Calling listener.onDeepLinkingResult")
                options.listener.onDeepLinkingResult(result)
            }
        }

        override fun onAttributionFailure(p0: String?) {
            // No-op
        }
    }

    private val deepLinkListener = DeepLinkListener { deepLinkResult ->
        when (deepLinkResult.status) {
            DeepLinkResult.Status.FOUND -> {
                println("🔗 DEEPLINK [KMM-UDL] Status: FOUND")
                val deepLink = deepLinkResult.deepLink
                val clickEventValues = deepLink.clickEvent.toMap()
                println("🔗 DEEPLINK [KMM-UDL] clickEventValues: $clickEventValues")

                val deepLinkValue = deepLink.deepLinkValue ?: clickEventValues.getDeepLinkValue()
                println("🔗 DEEPLINK [KMM-UDL] deepLinkValue: $deepLinkValue")

                val destination = deepLinkValue?.parseDestination()
                println("🔗 DEEPLINK [KMM-UDL] destination: $destination")

                val result = KMMDeepLinkResult(
                    origin = clickEventValues.buildOrigin(
                        source = DeeplinkSource.UDL,
                        udlStatus = UdlStatus.FOUND,
                        gcdStatus = null,
                        isDeferred = deepLink.isDeferred
                    ),
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
                println("🔗 DEEPLINK [KMM-UDL] $result")
                println("🔗 DEEPLINK [KMM-UDL] =====================================")
                println("🔗 DEEPLINK [KMM-UDL] Calling listener.onDeepLinkingResult")
                options.listener.onDeepLinkingResult(result)
            }

            DeepLinkResult.Status.NOT_FOUND -> {
                println("🔗 DEEPLINK [KMM-UDL] Status: NOT_FOUND")
                options.listener.onDeepLinkingResult(null)
            }

            else -> {
                println("🔗 DEEPLINK [KMM-UDL] Status: ERROR")
                options.listener.onDeepLinkingError(deepLinkResult.error.toError())
            }
        }
    }

    actual fun initialize() {
        AppsFlyerLib.getInstance().apply {
            options.enableDebugLog?.let { setDebugLog(it) }
            options.minTimeBetweenSessions?.let { setMinTimeBetweenSessions(it) }
            if (options.appInviteOneLinkTemplateId != null) {
                // Set the OneLink template id for share invite links
                setAppInviteOneLink(options.appInviteOneLinkTemplateId)
            }
            subscribeForDeepLink(deepLinkListener)
            init(options.devAppKey, conversionListener, options.context as Context)
        }
    }

    actual fun setCustomerUserId(userId: String) {
        AppsFlyerLib.getInstance().setCustomerUserId(userId)
    }

    actual fun stop(isStopped: Boolean) {
        AppsFlyerLib.getInstance().stop(isStopped, options.context as Context)
    }

    actual fun start(onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        AppsFlyerLib.getInstance().start(options.context as Context,
            options.devAppKey,
            object : AppsFlyerRequestListener {
                override fun onSuccess() {
                    onSuccess.invoke()
                }

                override fun onError(p0: Int, p1: String) {
                    onError.invoke(
                        Throwable(
                            "Error code: $p0, Error message: $p1"
                        )
                    )
                }
            })
    }

    actual fun start() {
        if (options.oneLinkCustomDomains.orEmpty().isNotEmpty()) {
            AppsFlyerLib.getInstance().setOneLinkCustomDomain(
                *options.oneLinkCustomDomains?.toTypedArray().orEmpty()
            )
        }
        initialize()
        AppsFlyerLib.getInstance().start(options.context as Context, options.devAppKey)
    }
}