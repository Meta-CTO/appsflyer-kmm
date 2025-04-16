package com.metacto.kmm.appsflyer.util

import com.metacto.kmm.appsflyer.OneLinkService
import com.metacto.kmm.appsflyer.ShareLinkGenerator
import com.metacto.kmm.appsflyer.model.AppAttributionResult
import kotlinx.coroutines.CancellableContinuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

fun OneLinkService.getAppAttributionResult(
    attributions: Map<*, *>,
): AppAttributionResult {
    val isOrganic = attributions[AppsFlyerConstants.AF_STATUS] == AppsFlyerConstants.AF_ORGANIC_STATUS
    val extras = attributions.filter { it.key != AppsFlyerConstants.AF_STATUS && it.key != null } as Map<Any, Any?>

    return AppAttributionResult(
        isOrganic = isOrganic,
        extras = extras
    )
}

fun ShareLinkGenerator.generateDeepLinkValue(
    destination: String,
    channel: String?,
    referrerCustomerId: String?,
    baseDeepLink: String?,
    referrerName: String?,
    referrerUID: String?,
    campaign: String?,
    referrerImageURL: String?,
    parameters: Map<String, String>?
): String {
    val stringBuilder = StringBuilder()
    stringBuilder.append("${AppsFlyerConstants.DEEP_LINK_DESTINATION}=$destination")
    channel?.let {
        stringBuilder.append("__${AppsFlyerConstants.CHANNEL}=$it")
    }
    referrerCustomerId?.let {
        stringBuilder.append("__${AppsFlyerConstants.REFERRER_CUSTOMER_ID}=$it")
    }
    referrerName?.let {
        stringBuilder.append("__${AppsFlyerConstants.REFERRER_NAME}=$it")
    }
    referrerUID?.let {
        stringBuilder.append("__${AppsFlyerConstants.REFERRER_UID}=$it")
    }
    campaign?.let {
        stringBuilder.append("__${AppsFlyerConstants.CAMPAIGN}=$it")
    }
    baseDeepLink?.let {
        stringBuilder.append("__${AppsFlyerConstants.BASE_DEEP_LINK}=$it")
    }
    referrerImageURL?.let {
        stringBuilder.append("__${AppsFlyerConstants.REFERRER_IMAGE_URL}=$it")
    }

    parameters?.let {
        it.forEach { (key, value) ->
            stringBuilder.append("__$key=$value")
        }
    }

    return stringBuilder.toString()
}

fun <T> CancellableContinuation<T>.resumeIfActive(value: T) {
    if (isActive) resume(value)
}

fun CancellableContinuation<*>.exceptionIfActive(throwable: Throwable) {
    if (isActive) resumeWithException(throwable)
}
