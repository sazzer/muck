package uk.co.grahamcox.muck.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MuckServiceApplication

fun main(args: Array<String>) {
    runApplication<MuckServiceApplication>(*args)
}
