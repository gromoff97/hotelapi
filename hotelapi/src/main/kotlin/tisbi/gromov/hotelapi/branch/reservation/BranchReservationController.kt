package tisbi.gromov.hotelapi.branch.reservation

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/branches/{branch}/reservations")
class BranchReservationController(
    private val branchReservationService: BranchReservationService
) {
    @GetMapping
    fun list(
        @PathVariable branch: String,
        @RequestParam(required = false) guestId: Int?,
        @RequestParam(required = false) roomId: Int?,
        @RequestParam(required = false) from: LocalDate?,
        @RequestParam(required = false) to: LocalDate?
    ): List<BranchReservationDto> = branchReservationService.list(branch, guestId, roomId, from, to)

    @GetMapping("/{reservationId}")
    fun get(@PathVariable branch: String, @PathVariable reservationId: Int): BranchReservationDto =
        branchReservationService.get(branch, reservationId)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@PathVariable branch: String, @RequestBody request: BranchReservationRequest): BranchReservationDto =
        branchReservationService.create(branch, request)

    @PutMapping("/{reservationId}")
    fun update(
        @PathVariable branch: String,
        @PathVariable reservationId: Int,
        @RequestBody request: BranchReservationRequest
    ): BranchReservationDto = branchReservationService.update(branch, reservationId, request)

    @DeleteMapping("/{reservationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable branch: String, @PathVariable reservationId: Int) {
        branchReservationService.delete(branch, reservationId)
    }
}