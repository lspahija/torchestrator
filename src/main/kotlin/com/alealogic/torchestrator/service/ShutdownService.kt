package com.alealogic.torchestrator.service

import org.springframework.stereotype.Service
import javax.annotation.PreDestroy

@Service
class ShutdownService(val torManager: TorManager) {

    @PreDestroy
    fun killAllContainers() =
        torManager.killAllContainers()
}