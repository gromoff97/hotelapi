package tisbi.gromov.hotelapi.central.supplier_branch

interface CentralSupplierBranchService {
    fun listBySupplier(supplierId: Int): List<CentralSupplierBranchDto>
    fun link(supplierId: Int, branchId: Int): CentralSupplierBranchDto
    fun unlink(supplierId: Int, branchId: Int)
}