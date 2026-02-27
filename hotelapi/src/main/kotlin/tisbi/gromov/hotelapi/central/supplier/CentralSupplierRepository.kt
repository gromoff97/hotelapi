package tisbi.gromov.hotelapi.central.supplier

import org.jooq.DSLContext
import org.jooq.impl.DSL.trueCondition
import org.springframework.stereotype.Repository
import tisbi.gromov.hotelapi.jooq.central.Tables.SUPPLIER
import tisbi.gromov.hotelapi.jooq.central.tables.records.SupplierRecord

@Repository
class CentralSupplierRepository(private val ctx: DSLContext) {

    fun findAll(companyName: String?): List<SupplierRecord> {
        val condition = if (companyName.isNullOrBlank()) trueCondition() else SUPPLIER.COMPANY_NAME.likeIgnoreCase("%$companyName%")
        return ctx.selectFrom(SUPPLIER)
            .where(condition)
            .orderBy(SUPPLIER.SUPPLIER_ID)
            .fetch()
    }

    fun findById(supplierId: Int): SupplierRecord? =
        ctx.selectFrom(SUPPLIER)
            .where(SUPPLIER.SUPPLIER_ID.eq(supplierId))
            .fetchOne()

    fun insert(companyName: String): SupplierRecord =
        ctx.insertInto(SUPPLIER)
            .set(SUPPLIER.COMPANY_NAME, companyName)
            .returning()
            .fetchOne()!!

    fun update(supplierId: Int, companyName: String): SupplierRecord? =
        ctx.update(SUPPLIER)
            .set(SUPPLIER.COMPANY_NAME, companyName)
            .where(SUPPLIER.SUPPLIER_ID.eq(supplierId))
            .returning()
            .fetchOne()

    fun delete(supplierId: Int): Int =
        ctx.deleteFrom(SUPPLIER)
            .where(SUPPLIER.SUPPLIER_ID.eq(supplierId))
            .execute()
}
