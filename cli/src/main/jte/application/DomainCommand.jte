@param String domainPackage
@param String domain
@param String domainPrefix
package ${domainPackage}.application

import ${domainPackage}.domain.entity.${domain}
import me.elgregos.reakteves.application.Command
import java.util.*

sealed class ${domain}Command(open val ${domainPrefix}Id: UUID) : Command {

    data class Create${domain}(
        val ${domainPrefix}: ${domain}
    ) : ${domain}Command(${domainPrefix}Id = ${domainPrefix}.id)

}