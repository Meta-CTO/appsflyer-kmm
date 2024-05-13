package com.metacto.kmm.appsflyer.model

import platform.Foundation.NSError

actual data class DeepLinkError(
    val error: NSError?
)