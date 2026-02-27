package tisbi.gromov.hotelapi.branch.room

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tisbi.gromov.hotelapi.utils.DatabaseRecordNotFoundException
import tisbi.gromov.hotelapi.utils.orNotFound
import tisbi.gromov.hotelapi.utils.toDto
import java.math.BigDecimal

@Service
class BranchRoomServiceImpl(
    private val repo: BranchRoomRepository
) : BranchRoomService {

    override fun list(branch: String, type: String?, maxPrice: BigDecimal?): List<BranchRoomDto> {
        return repo.findAll(branch, type, maxPrice).map { it.toDto() }
    }

    override fun get(branch: String, roomId: Int): BranchRoomDto {
        return repo.findById(branch, roomId)
            .orNotFound("Номер с id = $roomId не найден")
            .toDto()
    }

    @Transactional
    override fun create(branch: String, request: BranchRoomRequest): BranchRoomDto {
        return repo.insert(branch, request.number, request.type, request.price).toDto()
    }

    @Transactional
    override fun delete(branch: String, roomId: Int) {
        val deleted = repo.delete(branch, roomId)
        if (deleted == 0) throw DatabaseRecordNotFoundException("Номер с id = $roomId не найден")
    }

    @Transactional
    override fun update(branch: String, roomId: Int, request: BranchRoomRequest): BranchRoomDto {
        return repo.update(branch, roomId, request.number, request.type, request.price)
            .orNotFound("Номер с id = $roomId не найден")
            .toDto()
    }
}
