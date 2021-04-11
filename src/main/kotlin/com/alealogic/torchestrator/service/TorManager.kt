package com.alealogic.torchestrator.service

import com.alealogic.torchestrator.config.TorConfiguration
import com.alealogic.torchestrator.model.TorContainer
import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.async.ResultCallback
import com.github.dockerjava.api.command.BuildImageResultCallback
import com.github.dockerjava.api.model.BuildResponseItem
import com.github.dockerjava.api.model.HostConfig
import com.github.dockerjava.core.DockerClientBuilder
import okhttp3.internal.wait
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.*
import java.net.InetSocketAddress
import java.net.Socket
import java.util.*

@Service
class TorManager(
    private val torConfiguration: TorConfiguration,
    private val portService: PortService,
) {

    private val logger = LoggerFactory.getLogger(TorManager::class.java)
    private val dockerClient: DockerClient = DockerClientBuilder.getInstance().build()
    private val containerQueue: Queue<TorContainer> = initializeTorContainers()

    fun getNextProxyPort() =
        containerQueue
            .remove()
            .also { containerQueue.offer(it) }
            .httpProxyPort

    fun killAllContainers() =
        containerQueue
            .forEach { it.shutDown(dockerClient) }

    private fun imageExists() =
        dockerClient.listImagesCmd()
            .exec()
            .map { it.repoTags }
            .filterNotNull()
            .flatMap{ it.toList() }
            .contains(torConfiguration.imageName)


    private fun buildImage(){
        if (imageExists()) return

        dockerClient.buildImageCmd()
            .withDockerfile(File("docker/torprivoxydocker/Dockerfile"))
            .withPull(true)
            .withNoCache(true)
            .withTags(setOf(torConfiguration.imageName))
            .start()
            .run { awaitCompletion() }
            .also { imageExists() }
    }

    private fun initializeTorContainers() =
        buildImage()
            .run { (1..torConfiguration.containerQuantity) }
            .map { portService.getThreeAvailablePorts().iterator() }
            .map { runTorContainer(it.next(), it.next(), it.next()) }
            .onEach { authenticateTor(it) }
            .toCollection(LinkedList())


    private fun runTorContainer(torPort: Int, controlPort: Int, httpProxyPort: Int): TorContainer {
        val container = dockerClient.createContainerCmd(torConfiguration.imageName)
            .withHostConfig(HostConfig().withNetworkMode("host"))
            .withCmd(torPort.toString(), controlPort.toString(), httpProxyPort.toString())
            .exec()

        dockerClient.startContainerCmd(container.id).exec()
        return TorContainer(container.id, torPort, controlPort, httpProxyPort)
    }

    private fun authenticateTor(torContainer: TorContainer) {
        var authenticated = false

        while (!authenticated) {
            try {
                val controlSocket = Socket()
                    .also { it.connect(InetSocketAddress("127.0.0.1", torContainer.controlPort)) }

                val socketWriter = DataOutputStream(controlSocket.getOutputStream())
                    .also { it.write("AUTHENTICATE \"${torConfiguration.controlPassword}\"\r\n".toByteArray()) }
                    .also { it.flush() }

                val socketReader = BufferedReader(InputStreamReader(controlSocket.getInputStream()))
                val response = socketReader.readLine()!!
                logger.info("tor authentication response: $response")

                if (response == "250 OK") {
                    closeAll(controlSocket, socketReader, socketWriter)
                    authenticated = true
                } else
                    logger.error(response)

            } catch (e: IOException) {
                logger.warn(e.message)
                Thread.sleep(1000)
            }
        }
    }

    private fun closeAll(vararg closeables: Closeable) =
        closeables.forEach { it.close() }
}
