package com.alealogic.torchestrator.controller

import com.alealogic.torchestrator.service.TorManager
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PortController(val torManager: TorManager) {

    @GetMapping("port")
    fun getNextPort() = torManager.getNextProxyPort()

}
