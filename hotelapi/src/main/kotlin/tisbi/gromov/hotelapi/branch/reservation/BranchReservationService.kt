package tisbi.gromov.hotelapi.branch.reservation

import java.time.LocalDate

interface BranchReservationService {
    fun list(
        branch: String,
        guestId: Int? = null,
        roomId: Int? = null,
        from: LocalDate? = null,
        to: LocalDate? = null
    ): List<BranchReservationDto>

    fun get(branch: String, reservationId: Int): BranchReservationDto
    fun create(branch: String, request: BranchReservationRequest): BranchReservationDto
    fun update(branch: String, reservationId: Int, request: BranchReservationRequest): BranchReservationDto
    fun delete(branch: String, reservationId: Int)
}