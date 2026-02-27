package tisbi.gromov.hotelapi.branch.guest

data class BranchGuestDto(
    val guestId: Int,
    val fullName: String,
    val phone: String?
)