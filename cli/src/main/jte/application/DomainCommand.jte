@param String domainPackage
@param String domain
@param String domainPrefix
package ${domainPackage}.application

import me.elgregos.reakteves.application.Command
import me.elgregos.reakteves.libs.nowUTC
import java.time.LocalDateTime
import java.util.*

sealed class ${domain}Command(open val ${domainPrefix}Id: UUID) : Command {

    data class Create${domain}(
        override val ${domainPrefix}Id: UUID = UUID.randomUUID(),
        val createdBy: UUID,
        val createdAt: LocalDateTime = nowUTC()
    ) : ${domain}Command(${domainPrefix}Id)

}