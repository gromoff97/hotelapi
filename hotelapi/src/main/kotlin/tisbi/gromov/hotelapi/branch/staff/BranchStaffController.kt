package tisbi.gromov.hotelapi.branch.staff

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
@RequestMapping("/api/v1/branches/{branch}/staff")
class BranchStaffController(
    private val branchStaffService: BranchStaffService
) {
    @GetMapping
    fun list(@PathVariable branch: String): List<BranchStaffDto> =
        branchStaffService.list(branch)

    @GetMapping("/{staffId}")
    fun get(@PathVariable branch: String, @PathVariable staffId: Int): BranchStaffDto =
        branchStaffService.get(branch, staffId)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@PathVariable branch: String, @RequestBody request: BranchStaffRequest): BranchStaffDto =
        branchStaffService.create(branch, request)

    @PutMapping("/{staffId}")
    fun update(
        @PathVariable branch: String,
        @PathVariable staffId: Int,
        @RequestBody request: BranchStaffRequest
    ): BranchStaffDto = branchStaffService.update(branch, staffId, request)

    @DeleteMapping("/{staffId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable branch: String, @PathVariable staffId: Int) {
        branchStaffService.delete(branch, staffId)
    }
}