package tisbi.gromov.hotelapi.branch.reservation_staff

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/branches/{branch}/reservations/{reservationId}/staff")
class BranchReservationStaffController(
    private val branchReservationStaffService: BranchReservationStaffService
) {
    @GetMapping
    fun list(@PathVariable branch: String, @PathVariable reservationId: Int): List<BranchReservationStaffDto> =
        branchReservationStaffService.listStaffForReservation(branch, reservationId)

    @PostMapping("/{staffId}")
    @ResponseStatus(HttpStatus.CREATED)
    fun link(
        @PathVariable branch: String,
        @PathVariable reservationId: Int,
        @PathVariable staffId: Int
    ) {
        branchReservationStaffService.link(branch, reservationId, staffId)
    }

    @DeleteMapping("/{staffId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun unlink(
        @PathVariable branch: String,
        @PathVariable reservationId: Int,
        @PathVariable staffId: Int
    ) {
        branchReservationStaffService.unlink(branch, reservationId, staffId)
    }
}