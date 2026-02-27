package tisbi.gromov.hotelapi.central.supplier

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tisbi.gromov.hotelapi.jooq.central.tables.records.SupplierRecord
import tisbi.gromov.hotelapi.utils.DatabaseRecordNotFoundException
import tisbi.gromov.hotelapi.utils.orNotFound
import tisbi.gromov.hotelapi.utils.toDto

@Service
class CentralSupplierServiceImpl(
    private val repo: CentralSupplierRepository
) : CentralSupplierService {

    override fun list(companyName: String?) =
        repo.findAll(companyName).map(SupplierRecord::toDto)

    override fun get(supplierId: Int) =
        repo.findById(supplierId)
            .orNotFound("Поставщик с id = $supplierId не найден")
            .toDto()

    @Transactional
    override fun create(req: CentralSupplierCreateRequest) =
        repo.insert(req.companyName).toDto()

    @Transactional
    override fun update(supplierId: Int, req: CentralSupplierUpdateRequest) =
        repo.update(supplierId, req.companyName)
            .orNotFound("Поставщик с id = $supplierId не найден")
            .toDto()

    @Transactional
    override fun delete(supplierId: Int) {
        val deleted = repo.delete(supplierId)
        if (deleted == 0) throw DatabaseRecordNotFoundException("Поставщик с id = $supplierId не найден")
    }
}
