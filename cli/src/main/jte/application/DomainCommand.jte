@param String domainPackage
@param String domain
@param String domainPrefix
package ${domainPackage}.application

import me.elgregos.reakteves.application.Command
import me.elgregos.reakteves.libs.nowUTC
import me.elgregos.reakteves.libs.uuidV7
import java.time.LocalDateTime
import java.util.*

sealed class ${domain}Command(open val ${domainPrefix}Id: UUID) : Command {

    data class Create${domain}(
        override val ${domainPrefix}Id: UUID = uuidv7(),
        val createdBy: UUID,
        val createdAt: LocalDateTime = nowUTC()
    ) : ${domain}Command(${domainPrefix}Id)

}