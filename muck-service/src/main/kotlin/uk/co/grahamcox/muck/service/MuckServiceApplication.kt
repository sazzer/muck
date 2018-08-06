package uk.co.grahamcox.muck.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import uk.co.grahamcox.muck.service.database.DatabaseConfig

@SpringBootApplication
@Import(
        DatabaseConfig::class
)
class MuckServiceApplication

fun main(args: Array<String>) {
    runApplication<MuckServiceApplication>(*args)
}
