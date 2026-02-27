package tisbi.gromov.hotelapi.branch.reservation_staff

interface BranchReservationStaffService {
    fun listStaffForReservation(branch: String, reservationId: Int): List<BranchReservationStaffDto>
    fun link(branch: String, reservationId: Int, staffId: Int)
    fun unlink(branch: String, reservationId: Int, staffId: Int)
}