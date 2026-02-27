package tisbi.gromov.hotelapi.branch.reservation

import org.jooq.Condition
import org.jooq.impl.DSL.trueCondition
import org.springframework.stereotype.Repository
import tisbi.gromov.hotelapi.branch.BranchDslContextProvider
import tisbi.gromov.hotelapi.jooq.branch.Tables.RESERVATION
import tisbi.gromov.hotelapi.jooq.branch.tables.records.ReservationRecord
import java.time.LocalDate

@Repository
class BranchReservationRepository(
    private val dslProvider: BranchDslContextProvider
) {

    private fun ctx(branch: String) = dslProvider.dsl(branch)

    fun findAll(
        branch: String,
        guestId: Int?,
        roomId: Int?,
        checkInFrom: LocalDate?,
        checkInTo: LocalDate?
    ): List<ReservationRecord> {
        var condition: Condition = trueCondition()
        if (guestId != null) {
            condition = condition.and(RESERVATION.GUEST_ID.eq(guestId))
        }
        if (roomId != null) {
            condition = condition.and(RESERVATION.ROOM_ID.eq(roomId))
        }
        if (checkInFrom != null) {
            condition = condition.and(RESERVATION.CHECK_IN.ge(checkInFrom))
        }
        if (checkInTo != null) {
            condition = condition.and(RESERVATION.CHECK_IN.le(checkInTo))
        }
        return ctx(branch).selectFrom(RESERVATION)
            .where(condition)
            .orderBy(RESERVATION.RESERVATION_ID)
            .fetch()
    }

    fun findById(branch: String, reservationId: Int): ReservationRecord? =
        ctx(branch).selectFrom(RESERVATION)
            .where(RESERVATION.RESERVATION_ID.eq(reservationId))
            .fetchOne()

    fun insert(
        branch: String,
        guestId: Int,
        roomId: Int,
        checkIn: LocalDate,
        checkOut: LocalDate
    ): ReservationRecord =
        ctx(branch).insertInto(RESERVATION)
            .set(RESERVATION.GUEST_ID, guestId)
            .set(RESERVATION.ROOM_ID, roomId)
            .set(RESERVATION.CHECK_IN, checkIn)
            .set(RESERVATION.CHECK_OUT, checkOut)
            .returning()
            .fetchOne()!!

    fun update(
        branch: String,
        reservationId: Int,
        guestId: Int,
        roomId: Int,
        checkIn: LocalDate,
        checkOut: LocalDate
    ): ReservationRecord? =
        ctx(branch).update(RESERVATION)
            .set(RESERVATION.GUEST_ID, guestId)
            .set(RESERVATION.ROOM_ID, roomId)
            .set(RESERVATION.CHECK_IN, checkIn)
            .set(RESERVATION.CHECK_OUT, checkOut)
            .where(RESERVATION.RESERVATION_ID.eq(reservationId))
            .returning()
            .fetchOne()

    fun delete(branch: String, reservationId: Int): Int =
        ctx(branch).deleteFrom(RESERVATION)
            .where(RESERVATION.RESERVATION_ID.eq(reservationId))
            .execute()
}
