package com.alealogic.torchestrator.model

import com.github.dockerjava.api.DockerClient
import org.slf4j.LoggerFactory

data class TorContainer(
    val containerId: String,
    val torPort: Int,
    val controlPort: Int,
    val httpProxyPort: Int,
) {
    private val logger = LoggerFactory.getLogger(TorContainer::class.java)

    fun shutDown(dockerClient: DockerClient) {
        try {
            dockerClient.stopContainerCmd(containerId).exec()
            dockerClient.removeContainerCmd(containerId).exec()
        } catch (e: Exception) {
            logger.error(e.message, e)
        }
    }
}