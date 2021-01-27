package com.alealogic.torchestrator.controller

import com.alealogic.torchestrator.client.ExampleClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ExampleClientController(val exampleClient: ExampleClient) {

    @GetMapping("example")
    fun makeExampleCalls() = exampleClient.makeExampleCallsViaProxies(3)

}