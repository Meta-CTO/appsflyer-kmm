package com.metacto.kmm.appsflyer.model

data class Origin(
    val source: DeeplinkSource,
    val udlStatus: UdlStatus? = null,
    val status: GcdAfStatus? = null,
    val matchType: String? = null,
    val isDeferred: Boolean? = null
)

data class Campaign(
    val mediaSource: String? = null,
    val name: String? = null,
    val id: String? = null,
    val channel: String? = null,
    val siteId: String? = null,
    val subSiteId: String? = null,
    val partner: String? = null,
    val marketingPartner: String? = null,
    val agency: String? = null,
    val accountId: String? = null,
    val keywords: String? = null
)

data class Advertisement(
    val adset: String? = null,
    val adsetId: String? = null,
    val adgroup: String? = null,
    val adgroupId: String? = null,
    val adgroupName: String? = null,
    val ad: String? = null,
    val adId: String? = null,
    val adType: String? = null,
    val adFormat: String? = null,
    val adEventId: String? = null
)

data class DeepLink(
    val destination: String? = null,
    val value: String? = null,
    val sub1: String? = null,
    val sub2: String? = null,
    val sub3: String? = null,
    val sub4: String? = null,
    val sub5: String? = null,
    val sub6: String? = null,
    val sub7: String? = null,
    val sub8: String? = null,
    val sub9: String? = null,
    val sub10: String? = null,
    val scheme: String? = null,
    val forceDeeplink: Boolean? = null,
    val redirect: String? = null,
    val webRedirect: String? = null,
    val androidUrl: String? = null,
    val iosUrl: String? = null
)

data class Timestamps(
    val clickTime: String? = null,
    val installTime: String? = null,
    val isFirstLaunch: Boolean? = null,
    val clickLookback: String? = null,
    val viewthroughLookback: String? = null,
    val reengagementWindow: String? = null
)

data class Cost(
    val original: String? = null,
    val centsUsd: String? = null,
    val currency: String? = null,
    val value: String? = null,
    val model: String? = null,
    val perInstall: String? = null
)

data class Device(
    val os: String? = null,
    val osVersion: String? = null,
    val model: String? = null,
    val userAgent: String? = null,
    val ipAddress: String? = null,
    val mediaType: String? = null,
    val android: AndroidIdentifiers = AndroidIdentifiers(),
    val ios: IosIdentifiers = IosIdentifiers()
)

data class AndroidIdentifiers(
    val advertisingId: String? = null,
    val advertisingIdSha1: String? = null,
    val advertisingIdMd5: String? = null,
    val androidId: String? = null,
    val androidIdSha1: String? = null,
    val androidIdMd5: String? = null,
    val imei: String? = null,
    val imeiSha1: String? = null,
    val imeiMd5: String? = null,
    val oaid: String? = null,
    val oaidSha1: String? = null,
    val oaidMd5: String? = null,
    val fireAdvertisingId: String? = null,
    val emailSha1: String? = null,
    val storeListing: String? = null
)

data class IosIdentifiers(
    val idfa: String? = null,
    val idfaSha1: String? = null,
    val idfaMd5: String? = null,
    val idfv: String? = null,
    val idfvSha1: String? = null,
    val idfvMd5: String? = null,
    val mac: String? = null,
    val macSha1: String? = null,
    val productPage: String? = null
)

data class Retargeting(
    val isRetargeting: Boolean? = null,
    val conversionType: String? = null
)

data class Engagement(
    val isIncentivized: Boolean? = null,
    val clickId: String? = null,
    val referrer: String? = null
)

data class Viewability(
    val videoTotalLength: String? = null,
    val videoPlayedLength: String? = null,
    val playablePlayedLength: String? = null,
    val adTimeViewed: String? = null,
    val adDisplayedPercent: String? = null,
    val audioTotalLength: String? = null,
    val audioPlayedLength: String? = null
)

data class Network(
    val meta: MetaParameters? = null,
    val google: GoogleParameters? = null,
    val campaignType: String? = null
)

data class MetaParameters(
    val isFacebook: Boolean? = null,
    val isPaid: Boolean? = null,
    val isInstagram: Boolean? = null
)

data class GoogleParameters(
    val accountId: String? = null,
    val clickId: String? = null,
    val network: String? = null,
    val clickUrl: String? = null,
    val latitude: String? = null,
    val videoId: String? = null
)

data class CustomParameters(
    val sub1: String? = null,
    val sub2: String? = null,
    val sub3: String? = null,
    val sub4: String? = null,
    val sub5: String? = null
)

data class SocialPreview(
    val title: String? = null,
    val description: String? = null,
    val image: String? = null
)

data class System(
    val referralCode: String? = null,
    val parameterForwarding: Boolean? = null,
    val baseParametersForward: Boolean? = null
)

data class DeepLinkResult(
    val origin: Origin,
    val campaign: Campaign = Campaign(),
    val advertisement: Advertisement = Advertisement(),
    val deepLink: DeepLink = DeepLink(),
    val timestamps: Timestamps = Timestamps(),
    val cost: Cost = Cost(),
    val device: Device = Device(),
    val retargeting: Retargeting = Retargeting(),
    val engagement: Engagement = Engagement(),
    val viewability: Viewability = Viewability(),
    val network: Network = Network(),
    val customParameters: CustomParameters = CustomParameters(),
    val socialPreview: SocialPreview = SocialPreview(),
    val system: System = System(),
    val clickEvent: Map<Any?, *>? = null,
    val conversion: Map<Any?, *>? = null
) {
    companion object {
        fun notFound(): DeepLinkResult {
            return DeepLinkResult(
                origin = Origin(
                    source = DeeplinkSource.UDL,
                    udlStatus = UdlStatus.NOT_FOUND
                )
            )
        }
    }
}