package tisbi.gromov.hotelapi.branch.reservation_staff

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tisbi.gromov.hotelapi.utils.DatabaseRecordNotFoundException
import tisbi.gromov.hotelapi.utils.toDto

@Service
class BranchReservationStaffServiceImpl(
    private val repo: BranchReservationStaffRepository
) : BranchReservationStaffService {

    override fun listStaffForReservation(branch: String, reservationId: Int): List<BranchReservationStaffDto> {
        return repo.listByReservation(branch, reservationId).map { it.toDto() }
    }

    @Transactional
    override fun link(branch: String, reservationId: Int, staffId: Int) {
        repo.link(branch, reservationId, staffId)
    }

    @Transactional
    override fun unlink(branch: String, reservationId: Int, staffId: Int) {
        val deleted = repo.unlink(branch, reservationId, staffId)
        if (deleted == 0) {
            throw DatabaseRecordNotFoundException(
                "Связь бронирование=$reservationId сотрудник=$staffId не найдена"
            )
        }
    }
}
