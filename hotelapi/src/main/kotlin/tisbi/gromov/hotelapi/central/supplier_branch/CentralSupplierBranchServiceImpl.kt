package tisbi.gromov.hotelapi.central.supplier_branch

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tisbi.gromov.hotelapi.jooq.central.tables.records.SupplierBranchRecord
import tisbi.gromov.hotelapi.utils.DatabaseRecordNotFoundException
import tisbi.gromov.hotelapi.utils.toDto

@Service
class CentralSupplierBranchServiceImpl(
    private val repo: CentralSupplierBranchRepository
) : CentralSupplierBranchService {

    override fun listBySupplier(supplierId: Int) =
        repo.listBySupplier(supplierId).map(SupplierBranchRecord::toDto)

    @Transactional
    override fun link(supplierId: Int, branchId: Int) =
        repo.link(supplierId, branchId).toDto()

    @Transactional
    override fun unlink(supplierId: Int, branchId: Int) {
        val deleted = repo.unlink(supplierId, branchId)
        if (deleted == 0) {
            throw DatabaseRecordNotFoundException(
                "Связь поставщик=$supplierId филиал=$branchId не найдена"
            )
        }
    }
}
