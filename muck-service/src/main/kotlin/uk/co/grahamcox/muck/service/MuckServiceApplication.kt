package uk.co.grahamcox.muck.service

import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import uk.co.grahamcox.muck.service.spring.MuckConfig

@SpringBootConfiguration
@EnableAutoConfiguration
@Import(
        MuckConfig::class
)
class MuckServiceApplication

fun main(args: Array<String>) {
    runApplication<MuckServiceApplication>(*args)
}
