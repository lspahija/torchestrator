package com.alealogic.torchestrator

import com.alealogic.torchestrator.config.TorConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties(TorConfiguration::class)
@SpringBootApplication
class TorchestratorApplication

fun main(args: Array<String>) {
    runApplication<TorchestratorApplication>(*args)
}
