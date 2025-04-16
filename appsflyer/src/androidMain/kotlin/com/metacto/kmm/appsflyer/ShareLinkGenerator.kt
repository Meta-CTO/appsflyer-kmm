package com.metacto.kmm.appsflyer

import com.appsflyer.share.ShareInviteHelper
import com.metacto.kmm.appsflyer.model.BaseUrl
import com.metacto.kmm.appsflyer.util.AppsFlyerConstants
import com.metacto.kmm.appsflyer.util.generateDeepLinkValue

actual object ShareLinkGenerator {
    actual suspend fun generateShareLink(
        context: Any?,
        destination: String,
        channel: String?,
        referrerCustomerId: String?,
        referrerName: String?,
        referrerUID: String?,
        campaign: String?,
        baseDeepLink: String?,
        deepLinkPath: String?,
        referrerImageURL: String?,
        brandDomain: String?,
        baseURL: BaseUrl?,
        customShortLink: String?,
        parameters: Map<String, String>?
    ): String? {
        if (context == null || context !is android.content.Context) {
            throw IllegalArgumentException("Context is required and must be an instance of android.content.Context")
        }
        val generator = ShareInviteHelper.generateInviteUrl(context)

        val deepLinkValue = generateDeepLinkValue(
            destination,
            channel,
            referrerCustomerId,
            baseDeepLink,
            referrerName,
            referrerUID,
            campaign,
            referrerImageURL,
            parameters
        )

        generator.addParameter(AppsFlyerConstants.DEEP_LINK_VALUE, deepLinkValue)

        channel?.let {
            generator.setChannel(it)
        }
        referrerCustomerId?.let {
            generator.setReferrerCustomerId(it)
        }
        referrerName?.let {
            generator.setReferrerName(it)
        }
        referrerUID?.let {
            generator.setReferrerUID(it)
        }
        campaign?.let {
            generator.setCampaign(it)
        }
        baseDeepLink?.let {
            generator.setBaseDeeplink(it)
        }
        deepLinkPath?.let {
            generator.setDeeplinkPath(it)
        }
        referrerImageURL?.let {
            generator.setReferrerImageURL(it)
        }

        baseURL?.let {
            generator.setBaseURL(it.oneLinkID, it.domain, it.appPackage)
        }

        parameters?.let {
            generator.addParameters(it)
        }

        brandDomain?.let {
            generator.setBrandDomain(it)
        }
        baseURL?.let {
            generator.setBaseURL(it.oneLinkID, it.domain, it.appPackage)
        }
        parameters?.let {
            generator.addParameters(it)
        }
        customShortLink?.let {
            generator.addParameter("af_custom_shortlink", customShortLink)
        }

        return generator.generateLink()
    }
}
