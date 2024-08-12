package com.piroak.nyeok

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class CacheManager(
    private val globalApplication: GlobalApplication
) {
    // Cannot be private because it's used in the inline function
    val cacheDir: File
        get() = globalApplication.cacheDir

    fun clearCache() {
        cacheDir.deleteRecursively()
    }

    inline fun <reified T> getCache(key: String): T? {
        val file = cacheDir.resolve(key)
        return if (file.exists()) {
            Json.decodeFromString(file.readText())
        } else {
            null
        }
    }

    inline fun <reified T> setCache(key: String, value: T) {
        val file = cacheDir.resolve(key)
        file.writeText(Json.encodeToString(value))
    }
}