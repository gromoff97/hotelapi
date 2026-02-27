package tisbi.gromov.hotelapi.branch.room

import org.jooq.Condition
import org.jooq.impl.DSL.trueCondition
import org.springframework.stereotype.Repository
import tisbi.gromov.hotelapi.branch.BranchDslContextProvider
import tisbi.gromov.hotelapi.jooq.branch.Tables.ROOM
import tisbi.gromov.hotelapi.jooq.branch.tables.records.RoomRecord
import java.math.BigDecimal

@Repository
class BranchRoomRepository(
    private val dslProvider: BranchDslContextProvider
) {

    private fun ctx(branch: String) = dslProvider.dsl(branch)

    fun findAll(branch: String, type: String?, maxPrice: BigDecimal?): List<RoomRecord> {
        var condition: Condition = trueCondition()
        if (!type.isNullOrBlank()) {
            condition = condition.and(ROOM.TYPE.eq(type))
        }
        if (maxPrice != null) {
            condition = condition.and(ROOM.PRICE.le(maxPrice))
        }
        return ctx(branch).selectFrom(ROOM)
            .where(condition)
            .orderBy(ROOM.ROOM_ID)
            .fetch()
    }

    fun findById(branch: String, roomId: Int): RoomRecord? =
        ctx(branch).selectFrom(ROOM)
            .where(ROOM.ROOM_ID.eq(roomId))
            .fetchOne()

    fun insert(branch: String, number: String, type: String?, price: BigDecimal?): RoomRecord =
        ctx(branch).insertInto(ROOM)
            .set(ROOM.NUMBER, number)
            .set(ROOM.TYPE, type)
            .set(ROOM.PRICE, price)
            .returning()
            .fetchOne()!!

    fun update(branch: String, roomId: Int, number: String, type: String?, price: BigDecimal?): RoomRecord? =
        ctx(branch).update(ROOM)
            .set(ROOM.NUMBER, number)
            .set(ROOM.TYPE, type)
            .set(ROOM.PRICE, price)
            .where(ROOM.ROOM_ID.eq(roomId))
            .returning()
            .fetchOne()

    fun delete(branch: String, roomId: Int): Int =
        ctx(branch).deleteFrom(ROOM)
            .where(ROOM.ROOM_ID.eq(roomId))
            .execute()
}
