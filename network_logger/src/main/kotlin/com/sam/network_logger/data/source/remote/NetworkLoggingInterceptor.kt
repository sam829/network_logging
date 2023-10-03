package com.sam.network_logger.data.source.remote

import android.content.Context
import android.util.Log
import com.sam.network_logger.data.source.local.LoggerDatabase
import com.sam.network_logger.data.source.local.entity.NetworkCall
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class NetworkLoggingInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestNetworkCall = NetworkCall(
            requestHeader = request.headers.toString(),
            requestMethod = request.method,
            requestUrl = request.url.toString(),
            requestBody = request.body?.toString(),
            requestTag = request.tag().toString(),
            requestContentType = "${request.body?.contentType()?.type}/${request.body?.contentType()?.subtype}",
            requestContentLength = request.body?.contentLength(),
        )
        LoggerDatabase.getDatabase(context).networkCallDao().insertNetworkCall(requestNetworkCall)

        val startMs = System.nanoTime()

        val response: Response
        try {
            response = chain.proceed(request)
            val responseBody by lazy { response.peekBody(Long.MAX_VALUE).string() }

            val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startMs)

            val responseNetworkCallWithRequest = requestNetworkCall.copy(
                responseHeaders = response.headers.toString(),
                responseCode = response.code.toString(),
                responseMessage = response.message,
                responseProtocol = response.protocol.name,
                responseChallenge = response.challenges().toString(),
                responseBody = responseBody,
                responseContentLength = response.body?.contentLength(),
                responseContentType = "${response.body?.contentType()?.type}/${response.body?.contentType()?.subtype}",
                timeTaken = tookMs,
            )
            LoggerDatabase.getDatabase(context).networkCallDao()
                .insertNetworkCall(responseNetworkCallWithRequest)
        } catch (throwable: Throwable) {
            Log.e("NetworkLoggingInterceptor", "HTTP call failed", throwable)
            throw throwable
        }

        return response
    }
}