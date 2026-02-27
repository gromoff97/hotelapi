package tisbi.gromov.hotelapi.branch.guest

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/branches/{branch}/guests")
class BranchGuestController(
    private val branchGuestService: BranchGuestService
) {
    @GetMapping
    fun list(@PathVariable branch: String): List<BranchGuestDto> =
        branchGuestService.list(branch)

    @GetMapping("/{guestId}")
    fun get(@PathVariable branch: String, @PathVariable guestId: Int): BranchGuestDto =
        branchGuestService.get(branch, guestId)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@PathVariable branch: String, @RequestBody request: BranchGuestRequest): BranchGuestDto =
        branchGuestService.create(branch, request)

    @PutMapping("/{guestId}")
    fun update(
        @PathVariable branch: String,
        @PathVariable guestId: Int,
        @RequestBody request: BranchGuestRequest
    ): BranchGuestDto = branchGuestService.update(branch, guestId, request)

    @DeleteMapping("/{guestId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable branch: String, @PathVariable guestId: Int) {
        branchGuestService.delete(branch, guestId)
    }
}