package com.metacto.kmm.appsflyer

import com.metacto.kmm.appsflyer.model.BaseUrl

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
        customShortLink: String?,
        parameters: Map<String, String>? = null,
    ): String?
}