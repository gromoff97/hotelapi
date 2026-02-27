package tisbi.gromov.hotelapi.central.department

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tisbi.gromov.hotelapi.jooq.central.tables.records.DepartmentRecord
import tisbi.gromov.hotelapi.utils.DatabaseRecordNotFoundException
import tisbi.gromov.hotelapi.utils.orNotFound
import tisbi.gromov.hotelapi.utils.toDto

@Service
class CentralDepartmentServiceImpl(
    private val repo: CentralDepartmentRepository
) : CentralDepartmentService {

    override fun list(name: String?) =
        repo.findAll(name).map(DepartmentRecord::toDto)

    override fun get(departmentId: Int) =
        repo.findById(departmentId)
            .orNotFound("Департамент с id = $departmentId не найден")
            .toDto()

    @Transactional
    override fun create(req: CentralDepartmentCreateRequest) =
        repo.insert(req.name).toDto()

    @Transactional
    override fun update(departmentId: Int, req: CentralDepartmentUpdateRequest) =
        repo.update(departmentId, req.name)
            .orNotFound("Департамент с id = $departmentId не найден")
            .toDto()

    @Transactional
    override fun delete(departmentId: Int) {
        val deleted = repo.delete(departmentId)
        if (deleted == 0) throw DatabaseRecordNotFoundException("Департамент с id = $departmentId не найден")
    }
}
