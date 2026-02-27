package tisbi.gromov.hotelapi.central.branch

import org.springframework.stereotype.Service
import tisbi.gromov.hotelapi.jooq.central.tables.records.BranchRecord
import tisbi.gromov.hotelapi.utils.orNotFound
import tisbi.gromov.hotelapi.utils.toDto

@Service
class CentralBranchServiceImpl(
    private val repo: CentralBranchRepository
) : CentralBranchService {

    override fun list(): List<CentralBranchDto> =
        repo.findAll().map(BranchRecord::toDto)

    override fun get(branchId: Int): CentralBranchDto =
        repo.findById(branchId)
            .orNotFound("Филиал с id = $branchId не найден")
            .toDto()
}
