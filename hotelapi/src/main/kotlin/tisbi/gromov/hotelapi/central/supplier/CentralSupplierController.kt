package tisbi.gromov.hotelapi.central.supplier

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/central/suppliers")
class CentralSupplierController(
    private val centralSupplierService: CentralSupplierService
) {

    @GetMapping
    fun list(@RequestParam(required = false) companyName: String?): List<CentralSupplierDto> =
        centralSupplierService.list(companyName)

    @GetMapping("/{supplierId}")
    fun get(@PathVariable supplierId: Int): CentralSupplierDto =
        centralSupplierService.get(supplierId)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody req: CentralSupplierCreateRequest): CentralSupplierDto =
        centralSupplierService.create(req)

    @PutMapping("/{supplierId}")
    fun update(
        @PathVariable supplierId: Int,
        @RequestBody req: CentralSupplierUpdateRequest
    ): CentralSupplierDto =
        centralSupplierService.update(supplierId, req)

    @DeleteMapping("/{supplierId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable supplierId: Int) {
        centralSupplierService.delete(supplierId)
    }
}
