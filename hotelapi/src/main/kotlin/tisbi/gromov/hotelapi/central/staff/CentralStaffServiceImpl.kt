package tisbi.gromov.hotelapi.central.staff

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tisbi.gromov.hotelapi.jooq.central.tables.records.StaffCentralRecord
import tisbi.gromov.hotelapi.utils.DatabaseRecordNotFoundException
import tisbi.gromov.hotelapi.utils.orNotFound
import tisbi.gromov.hotelapi.utils.toDto

@Service
class CentralStaffServiceImpl(
    private val repo: CentralStaffRepository
) : CentralStaffService {

    override fun list(departmentId: Int?) =
        repo.findAll(departmentId).map(StaffCentralRecord::toDto)

    override fun get(staffId: Int) =
        repo.findById(staffId)
            .orNotFound("Сотрудник центрального узла с id = $staffId не найден")
            .toDto()

    @Transactional
    override fun create(req: CentralStaffCreateRequest) =
        repo.insert(req.name, req.departmentId).toDto()

    @Transactional
    override fun update(staffId: Int, req: CentralStaffUpdateRequest) =
        repo.update(staffId, req.name, req.departmentId)
            .orNotFound("Сотрудник центрального узла с id = $staffId не найден")
            .toDto()

    @Transactional
    override fun delete(staffId: Int) {
        val deleted = repo.delete(staffId)
        if (deleted == 0) throw DatabaseRecordNotFoundException("Сотрудник центрального узла с id = $staffId не найден")
    }
}
