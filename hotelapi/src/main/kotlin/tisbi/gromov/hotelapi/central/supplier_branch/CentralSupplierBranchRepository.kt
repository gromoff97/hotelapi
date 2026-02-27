package tisbi.gromov.hotelapi.central.supplier_branch

import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import tisbi.gromov.hotelapi.jooq.central.Tables.SUPPLIER_BRANCH
import tisbi.gromov.hotelapi.jooq.central.tables.records.SupplierBranchRecord

@Repository
class CentralSupplierBranchRepository(private val ctx: DSLContext) {

    fun listBySupplier(supplierId: Int): List<SupplierBranchRecord> =
        ctx.selectFrom(SUPPLIER_BRANCH)
            .where(SUPPLIER_BRANCH.SUPPLIER_ID.eq(supplierId))
            .orderBy(SUPPLIER_BRANCH.BRANCH_ID)
            .fetch()

    fun findById(supplierId: Int, branchId: Int): SupplierBranchRecord? =
        ctx.selectFrom(SUPPLIER_BRANCH)
            .where(
                SUPPLIER_BRANCH.SUPPLIER_ID.eq(supplierId)
                    .and(SUPPLIER_BRANCH.BRANCH_ID.eq(branchId))
            )
            .fetchOne()

    fun link(supplierId: Int, branchId: Int): SupplierBranchRecord =
        ctx.insertInto(SUPPLIER_BRANCH)
            .set(SUPPLIER_BRANCH.SUPPLIER_ID, supplierId)
            .set(SUPPLIER_BRANCH.BRANCH_ID, branchId)
            .returning()
            .fetchOne()!!

    fun unlink(supplierId: Int, branchId: Int): Int =
        ctx.deleteFrom(SUPPLIER_BRANCH)
            .where(
                SUPPLIER_BRANCH.SUPPLIER_ID.eq(supplierId)
                    .and(SUPPLIER_BRANCH.BRANCH_ID.eq(branchId))
            )
            .execute()
}
