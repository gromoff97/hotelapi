package tisbi.gromov.hotelapi.central.department

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/central/departments")
class CentralDepartmentController(
    private val centralDepartmentService: CentralDepartmentService
) {

    @GetMapping
    fun list(@RequestParam(required = false) name: String?): List<CentralDepartmentDto> =
        centralDepartmentService.list(name)

    @GetMapping("/{departmentId}")
    fun get(@PathVariable departmentId: Int): CentralDepartmentDto =
        centralDepartmentService.get(departmentId)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody req: CentralDepartmentCreateRequest): CentralDepartmentDto =
        centralDepartmentService.create(req)

    @PutMapping("/{departmentId}")
    fun update(
        @PathVariable departmentId: Int,
        @RequestBody req: CentralDepartmentUpdateRequest
    ): CentralDepartmentDto =
        centralDepartmentService.update(departmentId, req)

    @DeleteMapping("/{departmentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable departmentId: Int) {
        centralDepartmentService.delete(departmentId)
    }
}
