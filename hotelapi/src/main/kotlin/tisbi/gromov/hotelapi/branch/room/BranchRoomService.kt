package tisbi.gromov.hotelapi.branch.room

import java.math.BigDecimal

interface BranchRoomService {
    fun list(branch: String, type: String?, maxPrice: BigDecimal?): List<BranchRoomDto>
    fun get(branch: String, roomId: Int): BranchRoomDto
    fun create(branch: String, request: BranchRoomRequest): BranchRoomDto
    fun delete(branch: String, roomId: Int)
    fun update(branch: String, roomId: Int, request: BranchRoomRequest): BranchRoomDto
}