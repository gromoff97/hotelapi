package tisbi.gromov.hotelapi.branch.guest

import org.springframework.stereotype.Repository
import tisbi.gromov.hotelapi.branch.BranchDslContextProvider
import tisbi.gromov.hotelapi.jooq.branch.Tables.GUEST
import tisbi.gromov.hotelapi.jooq.branch.tables.records.GuestRecord

@Repository
class BranchGuestRepository(
    private val dslProvider: BranchDslContextProvider
) {

    private fun ctx(branch: String) = dslProvider.dsl(branch)

    fun findAll(branch: String): List<GuestRecord> =
        ctx(branch).selectFrom(GUEST)
            .orderBy(GUEST.GUEST_ID)
            .fetch()

    fun findById(branch: String, guestId: Int): GuestRecord? =
        ctx(branch).selectFrom(GUEST)
            .where(GUEST.GUEST_ID.eq(guestId))
            .fetchOne()

    fun insert(branch: String, fullName: String, phone: String?): GuestRecord =
        ctx(branch).insertInto(GUEST)
            .set(GUEST.FULL_NAME, fullName)
            .set(GUEST.PHONE, phone)
            .returning()
            .fetchOne()!!

    fun update(branch: String, guestId: Int, fullName: String, phone: String?): GuestRecord? =
        ctx(branch).update(GUEST)
            .set(GUEST.FULL_NAME, fullName)
            .set(GUEST.PHONE, phone)
            .where(GUEST.GUEST_ID.eq(guestId))
            .returning()
            .fetchOne()

    fun delete(branch: String, guestId: Int): Int =
        ctx(branch).deleteFrom(GUEST)
            .where(GUEST.GUEST_ID.eq(guestId))
            .execute()
}
