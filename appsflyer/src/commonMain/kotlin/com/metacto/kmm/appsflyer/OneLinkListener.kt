package com.metacto.kmm.appsflyer

import com.metacto.kmm.appsflyer.model.DeepLinkError
import com.metacto.kmm.appsflyer.model.DeepLinkResult

interface OneLinkListener {
    fun onDeepLinkingResult(result: DeepLinkResult)
    fun onDeepLinkNotFound(result: DeepLinkResult)
    fun onAttributionData(result: DeepLinkResult)
    fun onDeepLinkingError(error: DeepLinkError)
}