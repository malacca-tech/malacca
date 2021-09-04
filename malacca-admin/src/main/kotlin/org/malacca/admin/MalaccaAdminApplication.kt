package org.malacca.admin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MalaccaAdminApplication

fun main(args: Array<String>) {
    runApplication<MalaccaAdminApplication>(*args)
}
