package com.metacto.kmm.appsflyer.model

import com.appsflyer.deeplink.DeepLinkResult

actual open class DeepLinkError(
    open val message: String? = null
) {
    data object Timeout : DeepLinkError()
    data object Network : DeepLinkError()
    data object HttpStatusCode : DeepLinkError()
    data object Unexpected : DeepLinkError()
    data object DeveloperError : DeepLinkError()
    data class Generic(override val message: String) : DeepLinkError(message)
}


fun DeepLinkResult.Error.toError(): DeepLinkError {
    return when (this) {
        DeepLinkResult.Error.TIMEOUT -> DeepLinkError.Timeout
        DeepLinkResult.Error.NETWORK -> DeepLinkError.Network
        DeepLinkResult.Error.HTTP_STATUS_CODE -> DeepLinkError.HttpStatusCode
        DeepLinkResult.Error.UNEXPECTED -> DeepLinkError.Unexpected
        DeepLinkResult.Error.DEVELOPER_ERROR -> DeepLinkError.DeveloperError
    }
}