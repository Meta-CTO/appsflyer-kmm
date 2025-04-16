package com.metacto.kmm.appsflyer

import android.content.Context
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.appsflyer.attribution.AppsFlyerRequestListener
import com.appsflyer.deeplink.DeepLinkListener
import com.appsflyer.deeplink.DeepLinkResult
import com.metacto.kmm.appsflyer.model.getDeepLinkValue
import com.metacto.kmm.appsflyer.model.getDeepLinkMetadata
import com.metacto.kmm.appsflyer.model.getDestinationPath
import com.metacto.kmm.appsflyer.model.toError
import com.metacto.kmm.appsflyer.util.getAppAttributionResult

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
                val appConversionResult = this@OneLinkService.getAppAttributionResult(p0)
                options.listener.onAppAttribution(
                    appConversionResult.isOrganic,
                    appConversionResult.extras
                )
            }
        }

        override fun onConversionDataFail(p0: String?) {
            // No-op
        }

        override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {
            if (p0 != null) {
                val appConversionResult = this@OneLinkService.getAppAttributionResult(p0)
                options.listener.onAppAttribution(
                    appConversionResult.isOrganic,
                    appConversionResult.extras
                )
            }
        }

        override fun onAttributionFailure(p0: String?) {
            // No-op
        }
    }

    private val deepLinkListener = DeepLinkListener { deepLinkResult ->
        when (deepLinkResult.status) {
            DeepLinkResult.Status.FOUND -> {
                val deepLink = deepLinkResult.deepLink
                val clickEventValues = deepLink.clickEvent.toMap()
                val fullDeepLinkValue = deepLink.deepLinkValue ?: this.getDeepLinkValue(clickEventValues)
                val result = com.metacto.kmm.appsflyer.model.DeepLinkResult(
                    destination = fullDeepLinkValue?.let { this.getDestinationPath(fullDeepLinkValue) },
                    campaign = deepLink.campaign,
                    campaignId = deepLink.campaignId,
                    clickHttpReferrer = deepLink.clickHttpReferrer,
                    isDeferred = deepLink.isDeferred,
                    mediaSource = deepLink.mediaSource,
                    matchType = deepLink.matchType,
                    clickEventJson = deepLink.clickEvent.toString(),
                    metadata = this.getDeepLinkMetadata(fullDeepLinkValue, clickEventValues),
                )
                options.listener.onDeepLinkingResult(result)
            }

            DeepLinkResult.Status.NOT_FOUND -> {
                options.listener.onDeepLinkingResult(null)
            }

            else -> {
                options.listener.onDeepLinkingError(deepLinkResult.error.toError())
            }
        }
    }

    actual fun initialize() {
        AppsFlyerLib.getInstance().apply {
            options.enableDebugLog?.let { setDebugLog(it) }
            options.minTimeBetweenSessions?.let { setMinTimeBetweenSessions(it) }
            if (options.appInviteOneLinkTemplateId != null) {
                //set the OneLink template id for share invite links
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
        AppsFlyerLib.getInstance().start(options.context as Context, options.devAppKey,
    }
}