package com.metacto.kmm.appsflyer

import com.metacto.kmm.appsflyer.model.DeepLinkError
import com.metacto.kmm.appsflyer.model.DeepLinkResult

interface OneLinkListener {
    fun onAppAttribution(
        isOrganic: Boolean,
        extras: Map<Any, Any?>?
    )

    fun onDeepLinkingResult(result: DeepLinkResult?)
    fun onDeepLinkingError(error: DeepLinkError)
}