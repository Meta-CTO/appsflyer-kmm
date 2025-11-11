package com.metacto.kmm.appsflyer.model

import com.metacto.kmm.appsflyer.util.AppsFlyerConstants

fun Map<Any?, *>.getDeepLinkValue(): String? {
    return this[AppsFlyerConstants.DEEP_LINK_VALUE]?.toString()
}

fun String.parseDestination(): String? {
    if (!this.contains("__")) {
        return this
    }

    val values = this.split("__")
    return values.find { it.startsWith(AppsFlyerConstants.DEEP_LINK_DESTINATION) }?.substringAfter("=")
}

internal fun Map<Any?, *>.getString(key: String): String? =
    this[key]?.toString()?.takeIf { it != "null" && it.isNotEmpty() }

internal fun Map<Any?, *>.getBoolean(key: String): Boolean? {
    return when (val value = this[key]) {
        is Boolean -> value
        is String -> value.toBoolean()
        is Number -> value.toInt() != 0
        else -> null
    }
}

fun Map<Any?, *>.buildOrigin(source: DeeplinkSource, udlStatus: UdlStatus?, gcdStatus: GcdAfStatus?, isDeferred: Boolean?): Origin {
    return Origin(
        source = source,
        udlStatus = udlStatus,
        status = gcdStatus,
        matchType = getString(AppsFlyerConstants.MATCH_TYPE),
        isDeferred = isDeferred
    )
}

fun Map<Any?, *>.buildCampaign(): Campaign {
    return Campaign(
        mediaSource = getString(AppsFlyerConstants.MEDIA_SOURCE) ?: getString(AppsFlyerConstants.PID),
        name = getString(AppsFlyerConstants.CAMPAIGN) ?: getString(AppsFlyerConstants.C),
        id = getString(AppsFlyerConstants.CAMPAIGN_ID) ?: getString(AppsFlyerConstants.AF_C_ID),
        channel = getString(AppsFlyerConstants.AF_CHANNEL),
        siteId = getString(AppsFlyerConstants.AF_SITEID),
        subSiteId = getString(AppsFlyerConstants.AF_SUB_SITEID),
        partner = getString(AppsFlyerConstants.AF_PRT),
        marketingPartner = getString(AppsFlyerConstants.AF_MP),
        agency = getString(AppsFlyerConstants.AGENCY),
        accountId = getString(AppsFlyerConstants.AF_PARTNER_ACCOUNT_ID),
        keywords = getString(AppsFlyerConstants.AF_KEYWORDS)
    )
}

fun Map<Any?, *>.buildAdvertisement(): Advertisement {
    return Advertisement(
        adset = getString(AppsFlyerConstants.ADSET) ?: getString(AppsFlyerConstants.AF_ADSET),
        adsetId = getString(AppsFlyerConstants.ADSET_ID) ?: getString(AppsFlyerConstants.AF_ADSET_ID),
        adgroup = getString(AppsFlyerConstants.ADGROUP),
        adgroupId = getString(AppsFlyerConstants.ADGROUP_ID),
        adgroupName = getString(AppsFlyerConstants.ADGROUP_NAME),
        ad = getString(AppsFlyerConstants.AF_AD),
        adId = getString(AppsFlyerConstants.AD_ID) ?: getString(AppsFlyerConstants.AF_AD_ID),
        adType = getString(AppsFlyerConstants.AF_AD_TYPE),
        adFormat = getString(AppsFlyerConstants.AF_AD_FORMAT),
        adEventId = getString(AppsFlyerConstants.AD_EVENT_ID)
    )
}

fun Map<Any?, *>.buildDeepLink(destination: String?): DeepLink {
    return DeepLink(
        destination = destination,
        value = getDeepLinkValue(),
        sub1 = getString(AppsFlyerConstants.DEEP_LINK_SUB1),
        sub2 = getString(AppsFlyerConstants.DEEP_LINK_SUB2),
        sub3 = getString(AppsFlyerConstants.DEEP_LINK_SUB3),
        sub4 = getString(AppsFlyerConstants.DEEP_LINK_SUB4),
        sub5 = getString(AppsFlyerConstants.DEEP_LINK_SUB5),
        sub6 = getString(AppsFlyerConstants.DEEP_LINK_SUB6),
        sub7 = getString(AppsFlyerConstants.DEEP_LINK_SUB7),
        sub8 = getString(AppsFlyerConstants.DEEP_LINK_SUB8),
        sub9 = getString(AppsFlyerConstants.DEEP_LINK_SUB9),
        sub10 = getString(AppsFlyerConstants.DEEP_LINK_SUB10),
        scheme = getString(AppsFlyerConstants.BASE_DEEP_LINK),
        forceDeeplink = getBoolean(AppsFlyerConstants.AF_FORCE_DEEPLINK),
        redirect = getString(AppsFlyerConstants.AF_R),
        webRedirect = getString(AppsFlyerConstants.AF_WEB_DP),
        androidUrl = getString(AppsFlyerConstants.AF_ANDROID_URL),
        iosUrl = getString(AppsFlyerConstants.AF_IOS_URL)
    )
}

fun Map<Any?, *>.buildTimestamps(): Timestamps {
    return Timestamps(
        clickTime = getString(AppsFlyerConstants.CLICK_TIME),
        installTime = getString(AppsFlyerConstants.INSTALL_TIME),
        isFirstLaunch = getBoolean(AppsFlyerConstants.IS_FIRST_LAUNCH),
        clickLookback = getString(AppsFlyerConstants.AF_CLICK_LOOKBACK),
        viewthroughLookback = getString(AppsFlyerConstants.AF_VIEWTHROUGH_LOOKBACK),
        reengagementWindow = getString(AppsFlyerConstants.AF_REENGAGEMENT_WINDOW)
    )
}

fun Map<Any?, *>.buildCost(): Cost {
    return Cost(
        original = getString(AppsFlyerConstants.ORIG_COST),
        centsUsd = getString(AppsFlyerConstants.COST_CENTS_USD),
        currency = getString(AppsFlyerConstants.AF_COST_CURRENCY),
        value = getString(AppsFlyerConstants.AF_COST_VALUE),
        model = getString(AppsFlyerConstants.AF_COST_MODEL),
        perInstall = getString(AppsFlyerConstants.AF_CPI)
    )
}

fun Map<Any?, *>.buildDevice(): Device {
    return Device(
        os = getString(AppsFlyerConstants.AF_OS),
        osVersion = getString(AppsFlyerConstants.AF_OS_VERSION),
        model = getString(AppsFlyerConstants.AF_MODEL),
        userAgent = getString(AppsFlyerConstants.AF_UA),
        ipAddress = getString(AppsFlyerConstants.AF_IP),
        mediaType = getString(AppsFlyerConstants.AF_MEDIA_TYPE),
        android = buildAndroidIdentifiers(),
        ios = buildIosIdentifiers()
    )
}

fun Map<Any?, *>.buildAndroidIdentifiers(): AndroidIdentifiers {
    return AndroidIdentifiers(
        advertisingId = getString(AppsFlyerConstants.ADVERTISING_ID),
        advertisingIdSha1 = getString(AppsFlyerConstants.SHA1_ADVERTISING_ID),
        advertisingIdMd5 = getString(AppsFlyerConstants.MD5_ADVERTISING_ID),
        androidId = getString(AppsFlyerConstants.ANDROID_ID),
        androidIdSha1 = getString(AppsFlyerConstants.SHA1_ANDROID_ID),
        androidIdMd5 = getString(AppsFlyerConstants.MD5_ANDROID_ID),
        imei = getString(AppsFlyerConstants.IMEI),
        imeiSha1 = getString(AppsFlyerConstants.SHA1_IMEI),
        imeiMd5 = getString(AppsFlyerConstants.MD5_IMEI),
        oaid = getString(AppsFlyerConstants.OAID),
        oaidSha1 = getString(AppsFlyerConstants.SHA1_OAID),
        oaidMd5 = getString(AppsFlyerConstants.MD5_OAID),
        fireAdvertisingId = getString(AppsFlyerConstants.FIRE_ADVERTISING_ID),
        emailSha1 = getString(AppsFlyerConstants.SHA1_EL),
        storeListing = getString(AppsFlyerConstants.AF_ANDROID_STORE_CSL)
    )
}

fun Map<Any?, *>.buildIosIdentifiers(): IosIdentifiers {
    return IosIdentifiers(
        idfa = getString(AppsFlyerConstants.IDFA),
        idfaSha1 = getString(AppsFlyerConstants.SHA1_IDFA),
        idfaMd5 = getString(AppsFlyerConstants.MD5_IDFA),
        idfv = getString(AppsFlyerConstants.IDFV),
        idfvSha1 = getString(AppsFlyerConstants.SHA1_IDFV),
        idfvMd5 = getString(AppsFlyerConstants.MD5_IDFV),
        mac = getString(AppsFlyerConstants.MAC),
        macSha1 = getString(AppsFlyerConstants.SHA1_MAC),
        productPage = getString(AppsFlyerConstants.AF_IOS_STORE_CPP)
    )
}

fun Map<Any?, *>.buildRetargeting(): Retargeting {
    return Retargeting(
        isRetargeting = getBoolean(AppsFlyerConstants.IS_RETARGETING),
        conversionType = getString(AppsFlyerConstants.RETARGETING_CONVERSION_TYPE)
    )
}

fun Map<Any?, *>.buildEngagement(): Engagement {
    return Engagement(
        isIncentivized = getBoolean(AppsFlyerConstants.IS_INCENTIVIZED),
        clickId = getString(AppsFlyerConstants.CLICKID),
        referrer = getString(AppsFlyerConstants.HTTP_REFERRER)
    )
}

fun Map<Any?, *>.buildViewability(): Viewability {
    return Viewability(
        videoTotalLength = getString(AppsFlyerConstants.AF_VIDEO_TOTAL_LENGTH),
        videoPlayedLength = getString(AppsFlyerConstants.AF_VIDEO_PLAYED_LENGTH),
        playablePlayedLength = getString(AppsFlyerConstants.AF_PLAYABLE_PLAYED_LENGTH),
        adTimeViewed = getString(AppsFlyerConstants.AF_AD_TIME_VIEWED),
        adDisplayedPercent = getString(AppsFlyerConstants.AF_AD_DISPLAYED_PERCENT),
        audioTotalLength = getString(AppsFlyerConstants.AF_AUDIO_TOTAL_LENGTH),
        audioPlayedLength = getString(AppsFlyerConstants.AF_AUDIO_PLAYED_LENGTH)
    )
}

fun Map<Any?, *>.buildNetwork(): Network {
    return Network(
        meta = MetaParameters(
            isFacebook = getBoolean(AppsFlyerConstants.IS_FB),
            isPaid = getBoolean(AppsFlyerConstants.IS_PAID),
            isInstagram = getBoolean(AppsFlyerConstants.IS_INSTAGRAM)
        ),
        google = GoogleParameters(
            accountId = getString(AppsFlyerConstants.EXTERNAL_ACCOUNT_ID),
            clickId = getString(AppsFlyerConstants.GCLID),
            network = getString(AppsFlyerConstants.NETWORK),
            clickUrl = getString(AppsFlyerConstants.CLICK_URL),
            latitude = getString(AppsFlyerConstants.LAT),
            videoId = getString(AppsFlyerConstants.VIDEO_ID)
        ),
        campaignType = getString(AppsFlyerConstants.CAMPAIGN_TYPE)
    )
}

fun Map<Any?, *>.buildCustomParameters(): CustomParameters {
    return CustomParameters(
        sub1 = getString(AppsFlyerConstants.AF_SUB1),
        sub2 = getString(AppsFlyerConstants.AF_SUB2),
        sub3 = getString(AppsFlyerConstants.AF_SUB3),
        sub4 = getString(AppsFlyerConstants.AF_SUB4),
        sub5 = getString(AppsFlyerConstants.AF_SUB5)
    )
}

fun Map<Any?, *>.buildSocialPreview(): SocialPreview {
    return SocialPreview(
        title = getString(AppsFlyerConstants.AF_OG_TITLE),
        description = getString(AppsFlyerConstants.AF_OG_DESCRIPTION),
        image = getString(AppsFlyerConstants.AF_OG_IMAGE)
    )
}

fun Map<Any?, *>.buildSystem(): System {
    return System(
        referralCode = getString(AppsFlyerConstants.AF_REF),
        parameterForwarding = getBoolean(AppsFlyerConstants.AF_PARAM_FORWARDING),
        baseParametersForward = getBoolean(AppsFlyerConstants.AF_BASE_PARAMS_FORWARD)
    )
}

fun Map<Any?, *>.buildDeepLinkResult(
    source: DeeplinkSource,
    udlStatus: UdlStatus?,
    gcdStatus: GcdAfStatus?,
    isDeferred: Boolean?,
    destination: String?,
    clickEvent: Map<Any?, *>?,
    conversion: Map<Any?, *>?
): DeepLinkResult {
    return DeepLinkResult(
        origin = buildOrigin(source, udlStatus, gcdStatus, isDeferred),
        campaign = buildCampaign(),
        advertisement = buildAdvertisement(),
        deepLink = buildDeepLink(destination),
        timestamps = buildTimestamps(),
        cost = buildCost(),
        device = buildDevice(),
        retargeting = buildRetargeting(),
        engagement = buildEngagement(),
        viewability = buildViewability(),
        network = buildNetwork(),
        customParameters = buildCustomParameters(),
        socialPreview = buildSocialPreview(),
        system = buildSystem(),
        clickEvent = clickEvent,
        conversion = conversion
    )
}