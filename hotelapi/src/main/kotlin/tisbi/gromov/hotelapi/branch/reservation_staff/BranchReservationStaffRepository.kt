package tisbi.gromov.hotelapi.branch.reservation_staff

import org.springframework.stereotype.Repository
import tisbi.gromov.hotelapi.branch.BranchDslContextProvider
import tisbi.gromov.hotelapi.jooq.branch.Tables.RESERVATION_STAFF
import tisbi.gromov.hotelapi.jooq.branch.tables.records.ReservationStaffRecord

@Repository
class BranchReservationStaffRepository(
    private val dslProvider: BranchDslContextProvider
) {

    private fun ctx(branch: String) = dslProvider.dsl(branch)

    fun listByReservation(branch: String, reservationId: Int): List<ReservationStaffRecord> =
        ctx(branch).selectFrom(RESERVATION_STAFF)
            .where(RESERVATION_STAFF.RESERVATION_ID.eq(reservationId))
            .orderBy(RESERVATION_STAFF.STAFF_ID)
            .fetch()

    fun findAll(branch: String): List<ReservationStaffRecord> =
        ctx(branch).selectFrom(RESERVATION_STAFF)
            .orderBy(RESERVATION_STAFF.RESERVATION_ID, RESERVATION_STAFF.STAFF_ID)
            .fetch()

    fun findById(branch: String, reservationId: Int, staffId: Int): ReservationStaffRecord? =
        ctx(branch).selectFrom(RESERVATION_STAFF)
            .where(
                RESERVATION_STAFF.RESERVATION_ID.eq(reservationId)
                    .and(RESERVATION_STAFF.STAFF_ID.eq(staffId))
            )
            .fetchOne()

    fun link(branch: String, reservationId: Int, staffId: Int): ReservationStaffRecord =
        ctx(branch).insertInto(RESERVATION_STAFF)
            .set(RESERVATION_STAFF.RESERVATION_ID, reservationId)
            .set(RESERVATION_STAFF.STAFF_ID, staffId)
            .returning()
            .fetchOne()!!

    fun unlink(branch: String, reservationId: Int, staffId: Int): Int =
        ctx(branch).deleteFrom(RESERVATION_STAFF)
            .where(
                RESERVATION_STAFF.RESERVATION_ID.eq(reservationId)
                    .and(RESERVATION_STAFF.STAFF_ID.eq(staffId))
            )
            .execute()
}
