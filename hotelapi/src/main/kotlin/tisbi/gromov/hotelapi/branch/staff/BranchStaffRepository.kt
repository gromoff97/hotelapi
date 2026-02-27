package tisbi.gromov.hotelapi.branch.staff

import org.springframework.stereotype.Repository
import tisbi.gromov.hotelapi.branch.BranchDslContextProvider
import tisbi.gromov.hotelapi.jooq.branch.Tables.STAFF
import tisbi.gromov.hotelapi.jooq.branch.tables.records.StaffRecord

@Repository
class BranchStaffRepository(
    private val dslProvider: BranchDslContextProvider
) {

    private fun ctx(branch: String) = dslProvider.dsl(branch)

    fun findAll(branch: String): List<StaffRecord> =
        ctx(branch).selectFrom(STAFF)
            .orderBy(STAFF.STAFF_ID)
            .fetch()

    fun findById(branch: String, staffId: Int): StaffRecord? =
        ctx(branch).selectFrom(STAFF)
            .where(STAFF.STAFF_ID.eq(staffId))
            .fetchOne()

    fun insert(branch: String, name: String, role: String): StaffRecord =
        ctx(branch).insertInto(STAFF)
            .set(STAFF.NAME, name)
            .set(STAFF.ROLE, role)
            .returning()
            .fetchOne()!!

    fun update(branch: String, staffId: Int, name: String, role: String): StaffRecord? =
        ctx(branch).update(STAFF)
            .set(STAFF.NAME, name)
            .set(STAFF.ROLE, role)
            .where(STAFF.STAFF_ID.eq(staffId))
            .returning()
            .fetchOne()

    fun delete(branch: String, staffId: Int): Int =
        ctx(branch).deleteFrom(STAFF)
            .where(STAFF.STAFF_ID.eq(staffId))
            .execute()
}
