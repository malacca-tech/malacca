package org.malacca.engine

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MalaccaEngineApplication

fun main(args: Array<String>) {
    runApplication<MalaccaEngineApplication>(*args)
}
