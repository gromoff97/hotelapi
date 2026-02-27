package tisbi.gromov.hotelapi.branch.room

import java.math.BigDecimal

data class BranchRoomDto(
    val roomId: Int,
    val number: String,
    val type: String?,
    val price: BigDecimal?
)