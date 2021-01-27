package com.alealogic.torchestrator.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.IOException
import java.net.Socket

@Service
class PortService {

    private val logger = LoggerFactory.getLogger(PortService::class.java)

    private var portToAssign = 10060

    fun getThreeAvailablePorts(): List<Int> {
        val availablePorts = mutableListOf<Int>()

        while (availablePorts.size < 3)
            if (available(++portToAssign))
                availablePorts.add(portToAssign)

        return availablePorts
    }

    private fun available(port: Int): Boolean {
        return try {
            val s = Socket("localhost", port)
            try {
                s.close()
            } catch (e: IOException) {
                logger.error(e.message, e)
            }
            false
        } catch (e: IOException) {
            true
        }
    }
}