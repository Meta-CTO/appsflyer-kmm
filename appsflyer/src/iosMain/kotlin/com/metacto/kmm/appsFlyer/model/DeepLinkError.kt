package com.metacto.kmm.appsFlyer.model

import platform.Foundation.NSError

actual data class DeepLinkError(
    val error: NSError?
)