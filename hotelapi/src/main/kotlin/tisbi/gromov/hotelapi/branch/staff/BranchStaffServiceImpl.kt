package tisbi.gromov.hotelapi.branch.staff

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tisbi.gromov.hotelapi.utils.DatabaseRecordNotFoundException
import tisbi.gromov.hotelapi.utils.orNotFound
import tisbi.gromov.hotelapi.utils.toDto

@Service
class BranchStaffServiceImpl(
    private val repo: BranchStaffRepository
) : BranchStaffService {
    override fun list(branch: String): List<BranchStaffDto> {
        return repo.findAll(branch).map { it.toDto() }
    }

    override fun get(branch: String, staffId: Int): BranchStaffDto {
        return repo.findById(branch, staffId)
            .orNotFound("Сотрудник с id = $staffId не найден")
            .toDto()
    }

    @Transactional
    override fun create(branch: String, request: BranchStaffRequest): BranchStaffDto {
        return repo.insert(branch, request.name, request.role).toDto()
    }

    @Transactional
    override fun update(branch: String, staffId: Int, request: BranchStaffRequest): BranchStaffDto {
        return repo.update(branch, staffId, request.name, request.role)
            .orNotFound("Сотрудник с id = $staffId не найден")
            .toDto()
    }

    @Transactional
    override fun delete(branch: String, staffId: Int) {
        val deleted = repo.delete(branch, staffId)
        if (deleted == 0) throw DatabaseRecordNotFoundException("Сотрудник с id = $staffId не найден")
    }
}
