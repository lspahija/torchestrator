package com.alealogic.torchestrator.client

import com.alealogic.torchestrator.service.TorManager
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.net.InetSocketAddress
import java.net.Proxy
import java.time.Duration

@Component
class ExampleClient(
    val torManager: TorManager,
    @Value("\${test.url}") val testUrl: String
) {

    val baseHttpClient = createBaseHttpClient()

    fun makeExampleCallsViaProxies(count: Int) =
        (1..count)
            .asSequence()
            .map { getProxyClient(torManager.getNextProxyPort()) }
            .map {
                Request.Builder()
                    .url(testUrl)
                    .build() to it
            }
            .map { it.second.newCall(it.first).execute() }
            .map { it.body?.string()?.trim() }
            .toList()

    private fun getProxyClient(proxyPort: Int) =
        Proxy(Proxy.Type.HTTP, InetSocketAddress("localhost", proxyPort))
            .let { baseHttpClient.newBuilder().proxy(it).build() }

    private fun createBaseHttpClient() =
        Dispatcher()
            .apply {
                maxRequests = 50
                maxRequests = 50
            }
            .let {
                OkHttpClient.Builder()
                    .callTimeout(Duration.ofSeconds(700L))
                    .connectTimeout(Duration.ofSeconds(700L))
                    .readTimeout(Duration.ofSeconds(700L))
                    .writeTimeout(Duration.ofSeconds(700L))
                    .dispatcher(it)
                    .build()
            }

}