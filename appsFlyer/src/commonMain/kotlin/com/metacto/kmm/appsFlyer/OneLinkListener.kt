package com.metacto.kmm.appsFlyer

import com.metacto.kmm.appsFlyer.model.DeepLinkError
import com.metacto.kmm.appsFlyer.model.DeepLinkResult

interface OneLinkListener {
    fun onAppAttribution(
        isOrganic: Boolean,
        extras: Map<Any, Any?>?
    )

    fun onDeepLinkingResult(result: DeepLinkResult?)
    fun onDeepLinkingError(error: DeepLinkError)
}