package tisbi.gromov.hotelapi.central.staff

import org.jooq.DSLContext
import org.jooq.impl.DSL.trueCondition
import org.springframework.stereotype.Repository
import tisbi.gromov.hotelapi.jooq.central.Tables.STAFF_CENTRAL
import tisbi.gromov.hotelapi.jooq.central.tables.records.StaffCentralRecord

@Repository
class CentralStaffRepository(private val ctx: DSLContext) {

    fun findAll(departmentId: Int?): List<StaffCentralRecord> {
        val condition = if (departmentId == null) trueCondition() else STAFF_CENTRAL.DEPARTMENT_ID.eq(departmentId)
        return ctx.selectFrom(STAFF_CENTRAL)
            .where(condition)
            .orderBy(STAFF_CENTRAL.STAFF_ID)
            .fetch()
    }

    fun findById(staffId: Int): StaffCentralRecord? =
        ctx.selectFrom(STAFF_CENTRAL)
            .where(STAFF_CENTRAL.STAFF_ID.eq(staffId))
            .fetchOne()

    fun insert(name: String, departmentId: Int): StaffCentralRecord =
        ctx.insertInto(STAFF_CENTRAL)
            .set(STAFF_CENTRAL.NAME, name)
            .set(STAFF_CENTRAL.DEPARTMENT_ID, departmentId)
            .returning()
            .fetchOne()!!

    fun update(staffId: Int, name: String, departmentId: Int): StaffCentralRecord? =
        ctx.update(STAFF_CENTRAL)
            .set(STAFF_CENTRAL.NAME, name)
            .set(STAFF_CENTRAL.DEPARTMENT_ID, departmentId)
            .where(STAFF_CENTRAL.STAFF_ID.eq(staffId))
            .returning()
            .fetchOne()

    fun delete(staffId: Int): Int =
        ctx.deleteFrom(STAFF_CENTRAL)
            .where(STAFF_CENTRAL.STAFF_ID.eq(staffId))
            .execute()
}
