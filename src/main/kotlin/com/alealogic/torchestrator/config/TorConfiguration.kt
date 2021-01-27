package com.alealogic.torchestrator.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("tor")
data class TorConfiguration(
    val containerQuantity: Int,
    val imageName: String,
    val controlPassword: String
)