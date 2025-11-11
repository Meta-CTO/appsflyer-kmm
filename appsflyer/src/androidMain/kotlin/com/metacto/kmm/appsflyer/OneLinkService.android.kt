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
                val appConversionResult = this@OneLinkService.getAppAttributionResult(p0)
                @Suppress("UNCHECKED_CAST")
                val extras = appConversionResult.extras as Map<Any?, *>
                @Suppress("UNCHECKED_CAST")
                val conversion = p0 as Map<Any?, *>

                val isFirstLaunch = (extras["is_first_launch"] as? String)?.toBoolean()
                    ?: (extras["is_first_launch"] as? Boolean)
                    ?: false

                if (!isFirstLaunch) return

                val deepLinkValue = extras.getDeepLinkValue()
                val destination = deepLinkValue?.parseDestination()
                val gcdStatus = if (appConversionResult.isOrganic) GcdAfStatus.ORGANIC else GcdAfStatus.NON_ORGANIC

                val result = extras.buildDeepLinkResult(
                    source = DeeplinkSource.GCD,
                    udlStatus = null,
                    gcdStatus = gcdStatus,
                    isDeferred = true,
                    destination = destination,
                    clickEvent = extras,
                    conversion = conversion
                )

                options.listener.onAttributionData(result)
            }
        }

        override fun onConversionDataFail(p0: String?) {
            if (p0 != null) {
                options.listener.onDeepLinkingError(DeepLinkError.Generic(p0))
            }
        }

        override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {
            if (p0 != null) {
                val appConversionResult = this@OneLinkService.getAppAttributionResult(p0)
                @Suppress("UNCHECKED_CAST")
                val extras = appConversionResult.extras as Map<Any?, *>
                @Suppress("UNCHECKED_CAST")
                val conversion = p0 as Map<Any?, *>

                val deepLinkValue = extras.getDeepLinkValue()
                val destination = deepLinkValue?.parseDestination()
                val gcdStatus = if (appConversionResult.isOrganic) GcdAfStatus.ORGANIC else GcdAfStatus.NON_ORGANIC

                val result = extras.buildDeepLinkResult(
                    source = DeeplinkSource.GCD,
                    udlStatus = null,
                    gcdStatus = gcdStatus,
                    isDeferred = false,
                    destination = destination,
                    clickEvent = extras,
                    conversion = conversion
                )

                options.listener.onAttributionData(result)
            }
        }

        override fun onAttributionFailure(p0: String?) {
            if (p0 != null) {
                options.listener.onDeepLinkingError(DeepLinkError.Generic(p0))
            }
        }
    }

    private val deepLinkListener = DeepLinkListener { deepLinkResult ->
        when (deepLinkResult.status) {
            DeepLinkResult.Status.FOUND -> {
                val deepLink = deepLinkResult.deepLink
                val clickEventValues = deepLink.clickEvent.toMap()
                val deepLinkValue = deepLink.deepLinkValue ?: clickEventValues.getDeepLinkValue()
                val destination = deepLinkValue?.parseDestination()

                val result = clickEventValues.buildDeepLinkResult(
                    source = DeeplinkSource.UDL,
                    udlStatus = UdlStatus.FOUND,
                    gcdStatus = null,
                    isDeferred = deepLink.isDeferred,
                    destination = destination,
                    clickEvent = clickEventValues,
                    conversion = null
                )

                options.listener.onDeepLinkingResult(result)
            }

            DeepLinkResult.Status.NOT_FOUND -> {
                options.listener.onDeepLinkNotFound(KMMDeepLinkResult.notFound())
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