package com.example.githubuser.data.network

import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.internal.http2.ConnectionShutdownException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.jvm.Throws

class NetworkExceptionInterceptor : Interceptor {

    @Throws(Exception::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        try {
            val response = chain.proceed(request)
            if (response.isSuccessful) {
                return response.newBuilder().build()
            } else {
                throw NetworkException()
            }
        } catch (e: Exception) {
            var msg = ""
            when (e) {
                is SocketTimeoutException -> {
                    msg = "Timeout - Please check your internet connection"
                }

                is UnknownHostException -> {
                    msg = "Unable to make a connection. Please check your internet"
                }

                is ConnectionShutdownException -> {
                    msg = "Connection shutdown. Please check your internet"
                }

                is IOException -> {
                    msg = "Server is unreachable, please try again later."
                }

                is IllegalStateException -> {
                    msg = "${e.message}"
                }

                else -> {
                    msg = "${e.message}"
                }
            }
            return Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(999)
                .message(msg)
                .body("$e".toResponseBody(null))
                .build()
        }
    }
}
