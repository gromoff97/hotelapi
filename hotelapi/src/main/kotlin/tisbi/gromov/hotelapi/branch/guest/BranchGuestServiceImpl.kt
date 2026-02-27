package tisbi.gromov.hotelapi.branch.guest

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tisbi.gromov.hotelapi.utils.DatabaseRecordNotFoundException
import tisbi.gromov.hotelapi.utils.orNotFound
import tisbi.gromov.hotelapi.utils.toDto

@Service
class BranchGuestServiceImpl(
    private val repo: BranchGuestRepository
) : BranchGuestService {

    override fun list(branch: String): List<BranchGuestDto> {
        return repo.findAll(branch).map { it.toDto() }
    }

    override fun get(branch: String, guestId: Int): BranchGuestDto {
        return repo.findById(branch, guestId)
            .orNotFound("Гость с id = $guestId не найден")
            .toDto()
    }

    @Transactional
    override fun create(branch: String, request: BranchGuestRequest): BranchGuestDto {
        return repo.insert(branch, request.fullName, request.phone).toDto()
    }

    @Transactional
    override fun update(branch: String, guestId: Int, request: BranchGuestRequest): BranchGuestDto {
        return repo.update(branch, guestId, request.fullName, request.phone)
            .orNotFound("Гость с id = $guestId не найден")
            .toDto()
    }

    @Transactional
    override fun delete(branch: String, guestId: Int) {
        val deleted = repo.delete(branch, guestId)
        if (deleted == 0) throw DatabaseRecordNotFoundException("Гость с id = $guestId не найден")
    }
}
