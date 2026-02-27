package tisbi.gromov.hotelapi.central.supplier

interface CentralSupplierService {
    fun list(companyName: String?): List<CentralSupplierDto>
    fun get(supplierId: Int): CentralSupplierDto
    fun create(req: CentralSupplierCreateRequest): CentralSupplierDto
    fun update(supplierId: Int, req: CentralSupplierUpdateRequest): CentralSupplierDto
    fun delete(supplierId: Int)
}
