package tisbi.gromov.hotelapi.branch.guest

interface BranchGuestService {
    fun list(branch: String): List<BranchGuestDto>
    fun get(branch: String, guestId: Int): BranchGuestDto
    fun create(branch: String, request: BranchGuestRequest): BranchGuestDto
    fun update(branch: String, guestId: Int, request: BranchGuestRequest): BranchGuestDto
    fun delete(branch: String, guestId: Int)
}