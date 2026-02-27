package tisbi.gromov.hotelapi.branch

import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import tisbi.gromov.hotelapi.utils.DatabaseRecordNotFoundException

@Component
class BranchDslContextProvider(
    @Qualifier("branch1Dsl") private val branch1Dsl: DSLContext,
    @Qualifier("branch2Dsl") private val branch2Dsl: DSLContext
) {

    fun dsl(branch: String): DSLContext =
        when (branch.trim().lowercase()) {
            "branch1" -> branch1Dsl
            "branch2" -> branch2Dsl
            else -> throw DatabaseRecordNotFoundException("Филиал '$branch' не найден (ожидается: branch1 или branch2)")
        }
}
