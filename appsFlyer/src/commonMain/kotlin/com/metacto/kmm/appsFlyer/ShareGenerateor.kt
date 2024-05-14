package com.metacto.kmm.appsFlyer

import com.metacto.kmm.appsFlyer.model.BaseUrl

expect object ShareLinkGenerator {
    suspend fun generateShareLink(
        context: Any?,
        destination: String,
        channel: String? = null,
        referrerCustomerId: String? = null,
        referrerName: String? = null,
        referrerUID: String? = null,
        campaign: String? = null,
        baseDeepLink: String? = null,
        deepLinkPath: String? = null,
        referrerImageURL: String? = null,
        brandDomain: String? = null,
        baseURL: BaseUrl? = null,
        parameters: Map<String, String>? = null,
    ): String?
}