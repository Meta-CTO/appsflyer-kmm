package com.metacto.kmm.appsflyer.model

data class Origin(
    val source: DeeplinkSource,
    val udlStatus: UdlStatus?,
    val status: GcdAfStatus?,
    val matchType: String?,
    val isDeferred: Boolean?
)

data class Campaign(
    val mediaSource: String?,
    val name: String?,
    val id: String?,
    val channel: String?,
    val siteId: String?,
    val subSiteId: String?,
    val partner: String?,
    val marketingPartner: String?,
    val agency: String?,
    val accountId: String?,
    val keywords: String?
)

data class Advertisement(
    val adset: String?,
    val adsetId: String?,
    val adgroup: String?,
    val adgroupId: String?,
    val adgroupName: String?,
    val ad: String?,
    val adId: String?,
    val adType: String?,
    val adFormat: String?,
    val adEventId: String?
)

data class DeepLink(
    val destination: String?,
    val value: String?,
    val sub1: String?,
    val sub2: String?,
    val sub3: String?,
    val sub4: String?,
    val sub5: String?,
    val sub6: String?,
    val sub7: String?,
    val sub8: String?,
    val sub9: String?,
    val sub10: String?,
    val scheme: String?,
    val forceDeeplink: Boolean?,
    val redirect: String?,
    val webRedirect: String?,
    val androidUrl: String?,
    val iosUrl: String?
)

data class Timestamps(
    val clickTime: String?,
    val installTime: String?,
    val isFirstLaunch: Boolean?,
    val clickLookback: String?,
    val viewthroughLookback: String?,
    val reengagementWindow: String?
)

data class Cost(
    val original: String?,
    val centsUsd: String?,
    val currency: String?,
    val value: String?,
    val model: String?,
    val perInstall: String?
)

data class Device(
    val os: String?,
    val osVersion: String?,
    val model: String?,
    val userAgent: String?,
    val ipAddress: String?,
    val mediaType: String?,
    val android: AndroidIdentifiers,
    val ios: IosIdentifiers
)

data class AndroidIdentifiers(
    val advertisingId: String?,
    val advertisingIdSha1: String?,
    val advertisingIdMd5: String?,
    val androidId: String?,
    val androidIdSha1: String?,
    val androidIdMd5: String?,
    val imei: String?,
    val imeiSha1: String?,
    val imeiMd5: String?,
    val oaid: String?,
    val oaidSha1: String?,
    val oaidMd5: String?,
    val fireAdvertisingId: String?,
    val emailSha1: String?,
    val storeListing: String?
)

data class IosIdentifiers(
    val idfa: String?,
    val idfaSha1: String?,
    val idfaMd5: String?,
    val idfv: String?,
    val idfvSha1: String?,
    val idfvMd5: String?,
    val mac: String?,
    val macSha1: String?,
    val productPage: String?
)

data class Retargeting(
    val isRetargeting: Boolean?,
    val conversionType: String?
)

data class Engagement(
    val isIncentivized: Boolean?,
    val clickId: String?,
    val referrer: String?
)

data class Viewability(
    val videoTotalLength: String?,
    val videoPlayedLength: String?,
    val playablePlayedLength: String?,
    val adTimeViewed: String?,
    val adDisplayedPercent: String?,
    val audioTotalLength: String?,
    val audioPlayedLength: String?
)

data class Network(
    val meta: MetaParameters?,
    val google: GoogleParameters?,
    val campaignType: String?
)

data class MetaParameters(
    val isFacebook: Boolean?,
    val isPaid: Boolean?,
    val isInstagram: Boolean?
)

data class GoogleParameters(
    val accountId: String?,
    val clickId: String?,
    val network: String?,
    val clickUrl: String?,
    val latitude: String?,
    val videoId: String?
)

data class CustomParameters(
    val sub1: String?,
    val sub2: String?,
    val sub3: String?,
    val sub4: String?,
    val sub5: String?
)

data class SocialPreview(
    val title: String?,
    val description: String?,
    val image: String?
)

data class System(
    val referralCode: String?,
    val parameterForwarding: Boolean?,
    val baseParametersForward: Boolean?
)

data class DeepLinkResult(
    val origin: Origin,
    val campaign: Campaign,
    val advertisement: Advertisement,
    val deepLink: DeepLink,
    val timestamps: Timestamps,
    val cost: Cost,
    val device: Device,
    val retargeting: Retargeting,
    val engagement: Engagement,
    val viewability: Viewability,
    val network: Network,
    val customParameters: CustomParameters,
    val socialPreview: SocialPreview,
    val system: System,
    val clickEvent: Map<Any?, *>?,
    val conversion: Map<Any?, *>?
) {
    companion object {
        fun notFound(): DeepLinkResult {
            return DeepLinkResult(
                origin = Origin(
                    source = DeeplinkSource.UDL,
                    udlStatus = UdlStatus.NOT_FOUND,
                    status = null,
                    matchType = null,
                    isDeferred = null
                ),
                campaign = Campaign(
                    mediaSource = null,
                    name = null,
                    id = null,
                    channel = null,
                    siteId = null,
                    subSiteId = null,
                    partner = null,
                    marketingPartner = null,
                    agency = null,
                    accountId = null,
                    keywords = null
                ),
                advertisement = Advertisement(
                    adset = null,
                    adsetId = null,
                    adgroup = null,
                    adgroupId = null,
                    adgroupName = null,
                    ad = null,
                    adId = null,
                    adType = null,
                    adFormat = null,
                    adEventId = null
                ),
                deepLink = DeepLink(
                    destination = null,
                    value = null,
                    sub1 = null,
                    sub2 = null,
                    sub3 = null,
                    sub4 = null,
                    sub5 = null,
                    sub6 = null,
                    sub7 = null,
                    sub8 = null,
                    sub9 = null,
                    sub10 = null,
                    scheme = null,
                    forceDeeplink = null,
                    redirect = null,
                    webRedirect = null,
                    androidUrl = null,
                    iosUrl = null
                ),
                timestamps = Timestamps(
                    clickTime = null,
                    installTime = null,
                    isFirstLaunch = null,
                    clickLookback = null,
                    viewthroughLookback = null,
                    reengagementWindow = null
                ),
                cost = Cost(
                    original = null,
                    centsUsd = null,
                    currency = null,
                    value = null,
                    model = null,
                    perInstall = null
                ),
                device = Device(
                    os = null,
                    osVersion = null,
                    model = null,
                    userAgent = null,
                    ipAddress = null,
                    mediaType = null,
                    android = AndroidIdentifiers(
                        advertisingId = null,
                        advertisingIdSha1 = null,
                        advertisingIdMd5 = null,
                        androidId = null,
                        androidIdSha1 = null,
                        androidIdMd5 = null,
                        imei = null,
                        imeiSha1 = null,
                        imeiMd5 = null,
                        oaid = null,
                        oaidSha1 = null,
                        oaidMd5 = null,
                        fireAdvertisingId = null,
                        emailSha1 = null,
                        storeListing = null
                    ),
                    ios = IosIdentifiers(
                        idfa = null,
                        idfaSha1 = null,
                        idfaMd5 = null,
                        idfv = null,
                        idfvSha1 = null,
                        idfvMd5 = null,
                        mac = null,
                        macSha1 = null,
                        productPage = null
                    )
                ),
                retargeting = Retargeting(
                    isRetargeting = null,
                    conversionType = null
                ),
                engagement = Engagement(
                    isIncentivized = null,
                    clickId = null,
                    referrer = null
                ),
                viewability = Viewability(
                    videoTotalLength = null,
                    videoPlayedLength = null,
                    playablePlayedLength = null,
                    adTimeViewed = null,
                    adDisplayedPercent = null,
                    audioTotalLength = null,
                    audioPlayedLength = null
                ),
                network = Network(
                    meta = MetaParameters(
                        isFacebook = null,
                        isPaid = null,
                        isInstagram = null
                    ),
                    google = GoogleParameters(
                        accountId = null,
                        clickId = null,
                        network = null,
                        clickUrl = null,
                        latitude = null,
                        videoId = null
                    ),
                    campaignType = null
                ),
                customParameters = CustomParameters(
                    sub1 = null,
                    sub2 = null,
                    sub3 = null,
                    sub4 = null,
                    sub5 = null
                ),
                socialPreview = SocialPreview(
                    title = null,
                    description = null,
                    image = null
                ),
                system = System(
                    referralCode = null,
                    parameterForwarding = null,
                    baseParametersForward = null
                ),
                clickEvent = null,
                conversion = null
            )
        }
    }
}