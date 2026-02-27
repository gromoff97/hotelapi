package tisbi.gromov.hotelapi.central.staff

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/central/staff")
class CentralStaffController(
    private val centralStaffService: CentralStaffService
) {

    @GetMapping
    fun list(@RequestParam(required = false) departmentId: Int?): List<CentralStaffDto> =
        centralStaffService.list(departmentId)

    @GetMapping("/{staffId}")
    fun get(@PathVariable staffId: Int): CentralStaffDto =
        centralStaffService.get(staffId)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody req: CentralStaffCreateRequest): CentralStaffDto =
        centralStaffService.create(req)

    @PutMapping("/{staffId}")
    fun update(
        @PathVariable staffId: Int,
        @RequestBody req: CentralStaffUpdateRequest
    ): CentralStaffDto =
        centralStaffService.update(staffId, req)

    @DeleteMapping("/{staffId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable staffId: Int) {
        centralStaffService.delete(staffId)
    }
}
