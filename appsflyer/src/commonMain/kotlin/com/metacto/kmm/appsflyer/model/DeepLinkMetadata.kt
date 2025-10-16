package com.metacto.kmm.appsflyer.model

import com.metacto.kmm.appsflyer.OneLinkService
import com.metacto.kmm.appsflyer.util.AppsFlyerConstants
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

data class DeepLinkMetadata(
    val referrerName: String?,
    val baseDeepLinkPath: String?,
    val channel: String?,
    val campaign: String?,
    val referrerCustomerId: String?,
    val referrerUID: String?,
    val referrerImageURL: String?,
    val extras: Map<Any?, *>?
)

fun OneLinkService.getDeepLinkValue(values: Map<Any?, *>): String? {
    return values[AppsFlyerConstants.DEEP_LINK_VALUE]?.toString()
}

fun OneLinkService.getDestinationPath(fullDeepLinkValue: String): String? {
    return fullDeepLinkValue.parseJsonDestination() ?: fullDeepLinkValue.parseNormalDestination()
}

internal fun String.parseJsonDestination(): String? {
    return try {
        val destinationValue = Json.parseToJsonElement(this.trim()).let {
            it.jsonObject[AppsFlyerConstants.DEEP_LINK_DESTINATION]?.jsonPrimitive?.content
        }

        val values = destinationValue?.split("__")
        return values?.firstOrNull()
    } catch (_: Throwable) {
        null
    }
}

internal fun String.parseNormalDestination(): String? {
    val values = this.split("__")
    return values.find { it.startsWith(AppsFlyerConstants.DEEP_LINK_DESTINATION) }?.substringAfter("=")
}

fun OneLinkService.getDeepLinkMetadata(deepLinkValue: String?, extraValues: Map<Any?, *>): DeepLinkMetadata {
    val values = deepLinkValue?.split("__").orEmpty()
    val referrerName =
        values.find { it.startsWith(AppsFlyerConstants.REFERRER_NAME) }?.substringAfter("=")
    val baseDeepLinkPath =
        values.find { it.startsWith(AppsFlyerConstants.BASE_DEEP_LINK) }?.substringAfter("=")
    val channel = values.find { it.startsWith(AppsFlyerConstants.CHANNEL) }?.substringAfter("=")
    val campaign = values.find { it.startsWith(AppsFlyerConstants.CAMPAIGN) }?.substringAfter("=")
    val referrerCustomerId =
        values.find { it.startsWith(AppsFlyerConstants.REFERRER_CUSTOMER_ID) }?.substringAfter("=")
    val referrerUID =
        values.find { it.startsWith(AppsFlyerConstants.REFERRER_UID) }?.substringAfter("=")
    val referrerImageURL =
        values.find { it.startsWith(AppsFlyerConstants.REFERRER_IMAGE_URL) }?.substringAfter("=")
    val extras = values.filter {
        !it.startsWith(AppsFlyerConstants.REFERRER_NAME)
                && !it.startsWith(AppsFlyerConstants.DEEP_LINK_VALUE)
                && !it.startsWith(AppsFlyerConstants.BASE_DEEP_LINK)
                && !it.startsWith(AppsFlyerConstants.CHANNEL)
                && !it.startsWith(AppsFlyerConstants.CAMPAIGN)
                && !it.startsWith(AppsFlyerConstants.REFERRER_CUSTOMER_ID)
                && !it.startsWith(AppsFlyerConstants.DEEP_LINK_DESTINATION)
                && !it.startsWith(AppsFlyerConstants.REFERRER_UID)
                && !it.startsWith(AppsFlyerConstants.REFERRER_IMAGE_URL)
    }.map {
        it.substringBefore("=") to it.substringAfter("=")
    }

    val extraData = (extras.toMap() as Map<out Any?, *>).toMutableMap()
    extraData.putAll(extraValues)

    return DeepLinkMetadata(
        referrerName = referrerName,
        baseDeepLinkPath = baseDeepLinkPath,
        channel = channel,
        campaign = campaign,
        referrerCustomerId = referrerCustomerId,
        referrerUID = referrerUID,
        referrerImageURL = referrerImageURL,
        extras = extraData
    )
}