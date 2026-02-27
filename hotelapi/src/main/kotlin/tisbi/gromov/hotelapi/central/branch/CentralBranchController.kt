package tisbi.gromov.hotelapi.central.branch

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/central/branches")
class CentralBranchController(
    private val centralBranchService: CentralBranchService
) {

    @GetMapping
    fun list(): List<CentralBranchDto> =
        centralBranchService.list()

    @GetMapping("/{branchId}")
    fun get(@PathVariable branchId: Int): CentralBranchDto =
        centralBranchService.get(branchId)
}