package tisbi.gromov.hotelapi.central.supplier_branch

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/central/suppliers")
class CentralSupplierBranchController(
    private val centralSupplierBranchService: CentralSupplierBranchService
) {

    @GetMapping("/{supplierId}/branches")
    fun listBySupplier(@PathVariable supplierId: Int): List<CentralSupplierBranchDto> =
        centralSupplierBranchService.listBySupplier(supplierId)

    @PostMapping("/{supplierId}/branches/{branchId}")
    @ResponseStatus(HttpStatus.CREATED)
    fun link(
        @PathVariable supplierId: Int,
        @PathVariable branchId: Int
    ): CentralSupplierBranchDto =
        centralSupplierBranchService.link(supplierId, branchId)

    @DeleteMapping("/{supplierId}/branches/{branchId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun unlink(
        @PathVariable supplierId: Int,
        @PathVariable branchId: Int
    ) {
        centralSupplierBranchService.unlink(supplierId, branchId)
    }
}
