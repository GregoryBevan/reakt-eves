@param String domainPackage
@param String domain
@param String domainPath
@param String domainPrefix
package ${domainPackage}.api

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import ${domainPackage}.application.${domain}Command
import ${domainPackage}.application.${domain}CommandHandler
import ${domainPackage}.application.${domain}ProjectionService
import org.springframework.http.HttpStatus
import reactor.kotlin.core.publisher.toMono
import java.util.*

@RestController
@RequestMapping(
    path = ["/api/${domainPath}"]
)
class ${domain}Controller(
    private val ${domainPrefix}CommandHandler: ${domain}CommandHandler,
    private val ${domainPrefix}ProjectionService: ${domain}ProjectionService
) {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun ${domainPrefix}s() = ${domainPrefix}ProjectionService.${domainPrefix}s()

    @GetMapping("{${domainPrefix}Id}")
    @ResponseStatus(HttpStatus.OK)
    fun ${domainPrefix}(@PathVariable @Valid ${domainPrefix}Id: UUID) = ${domainPrefix}ProjectionService.${domainPrefix}(${domainPrefix}Id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create${domain}() =
        ${domainPrefix}CommandHandler.handle(${domain}Command.Create${domain}(createdBy = UUID.randomUUID()))
            .toMono()
            .map { mapOf(Pair("${domainPrefix}Id", it.aggregateId)) }

}