package tisbi.gromov.hotelapi.branch.room

import java.math.BigDecimal

data class BranchRoomRequest(
    val number: String,
    val type: String?,
    val price: BigDecimal?
)