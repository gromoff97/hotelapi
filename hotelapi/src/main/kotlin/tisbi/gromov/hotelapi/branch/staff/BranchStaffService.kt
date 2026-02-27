package tisbi.gromov.hotelapi.branch.staff

interface BranchStaffService {
    fun list(branch: String): List<BranchStaffDto>
    fun get(branch: String, staffId: Int): BranchStaffDto
    fun create(branch: String, request: BranchStaffRequest): BranchStaffDto
    fun update(branch: String, staffId: Int, request: BranchStaffRequest): BranchStaffDto
    fun delete(branch: String, staffId: Int)
}