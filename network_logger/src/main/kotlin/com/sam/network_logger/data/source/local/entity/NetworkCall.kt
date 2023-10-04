package com.sam.network_logger.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class NetworkCall(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    val requestHeader: String? = null,
    val requestMethod: String? = null,
    val requestUrl: String? = null,
    val requestBody: String? = null,
    val requestTag: String? = null,
    val requestContentType: String? = null,
    val requestContentLength: Long? = null,
    val responseHeaders: String? = null,
    val responseCode: String? = null,
    val responseMessage: String? = null,
    val responseProtocol: String? = null,
    val responseChallenge: String? = null,
    val responseBody: String? = null,
    val responseContentType: String? = null,
    val responseContentLength: Long? = null,
    val timeTaken: Long? = null,
    val isError: Boolean = true,
)
