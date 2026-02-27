package tisbi.gromov.hotelapi.central.branch

import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import tisbi.gromov.hotelapi.jooq.central.Tables.BRANCH
import tisbi.gromov.hotelapi.jooq.central.tables.records.BranchRecord

@Repository
class CentralBranchRepository(private val ctx: DSLContext) {

    fun findAll(): List<BranchRecord> = ctx.selectFrom(BRANCH).fetch()

    fun findById(branchId: Int): BranchRecord? = ctx.selectFrom(BRANCH)
        .where(BRANCH.BRANCH_ID.eq(branchId))
        .fetchOne()
}