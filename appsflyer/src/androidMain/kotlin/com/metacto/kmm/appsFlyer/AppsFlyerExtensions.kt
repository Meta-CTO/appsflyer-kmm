package com.metacto.kmm.appsFlyer

import org.json.JSONObject

fun JSONObject.toMap(): Map<Any?, *> {
    val map = mutableMapOf<Any?, Any?>()
    keys().forEach {
        map[it] = get(it)
    }

    return map
}