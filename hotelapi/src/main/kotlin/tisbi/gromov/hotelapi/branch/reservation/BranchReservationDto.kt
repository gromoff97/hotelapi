package tisbi.gromov.hotelapi.branch.reservation

import java.time.LocalDate

data class BranchReservationDto(
    val reservationId: Int,
    val guestId: Int,
    val roomId: Int,
    val checkIn: LocalDate,
    val checkOut: LocalDate
)