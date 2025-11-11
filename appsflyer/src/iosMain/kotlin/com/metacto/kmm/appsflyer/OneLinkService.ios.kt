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
            setAppsFlyerDevKey(options.devAppKey)

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
                val deepLinkValue = deepLink?.deeplinkValue ?: clickEventValues.getDeepLinkValue()
                val destination = deepLinkValue?.parseDestination()

                val deepLinkResult = clickEventValues.buildDeepLinkResult(
                    source = DeeplinkSource.UDL,
                    udlStatus = UdlStatus.FOUND,
                    gcdStatus = null,
                    isDeferred = deepLink?.isDeferred,
                    destination = destination,
                    clickEvent = clickEventValues,
                    conversion = null
                )

                options.listener.onDeepLinkingResult(deepLinkResult)
            }

            AFSDKDeepLinkResultStatus.AFSDKDeepLinkResultStatusNotFound -> {
                options.listener.onDeepLinkNotFound(DeepLinkResult.notFound())
            }

            AFSDKDeepLinkResultStatus.AFSDKDeepLinkResultStatusFailure -> {
                options.listener.onDeepLinkingError(DeepLinkError(result.error))
            }

            else -> {
                options.listener.onDeepLinkNotFound(DeepLinkResult.notFound())
            }
        }
    }

    override fun onConversionDataFail(error: NSError) {
        options.listener.onDeepLinkingError(DeepLinkError(error))
    }

    override fun onConversionDataSuccess(conversionInfo: Map<Any?, *>) {
        val appConversionResult = this.getAppAttributionResult(conversionInfo)
        @Suppress("UNCHECKED_CAST")
        val extras = appConversionResult.extras as Map<Any?, *>

        val isFirstLaunch = extras.getBoolean("is_first_launch") ?: false
        if (!isFirstLaunch) return

        val deepLinkValue = (conversionInfo["deep_link_value"] ?: extras["deep_link_value"])?.toString()
        val destination = deepLinkValue?.parseDestination()
        val gcdStatus = if (appConversionResult.isOrganic) GcdAfStatus.ORGANIC else GcdAfStatus.NON_ORGANIC

        val deepLinkResult = extras.buildDeepLinkResult(
            source = DeeplinkSource.GCD,
            udlStatus = null,
            gcdStatus = gcdStatus,
            isDeferred = isFirstLaunch,
            destination = destination,
            clickEvent = null,
            conversion = conversionInfo
        )

        options.listener.onAttributionData(deepLinkResult)
    }

    override fun onAppOpenAttributionFailure(error: NSError) {
        options.listener.onDeepLinkingError(DeepLinkError(error))
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