package tisbi.gromov.hotelapi.branch.reservation

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tisbi.gromov.hotelapi.utils.DatabaseRecordNotFoundException
import tisbi.gromov.hotelapi.utils.orNotFound
import tisbi.gromov.hotelapi.utils.toDto
import java.time.LocalDate

@Service
class BranchReservationServiceImpl(
    private val repo: BranchReservationRepository
) : BranchReservationService {

    override fun list(
        branch: String,
        guestId: Int?,
        roomId: Int?,
        from: LocalDate?,
        to: LocalDate?
    ): List<BranchReservationDto> {
        return repo.findAll(branch, guestId, roomId, from, to).map { it.toDto() }
    }

    override fun get(branch: String, reservationId: Int): BranchReservationDto {
        return repo.findById(branch, reservationId)
            .orNotFound("Бронирование с id = $reservationId не найдено")
            .toDto()
    }

    @Transactional
    override fun create(branch: String, request: BranchReservationRequest): BranchReservationDto {
        return repo.insert(
            branch,
            request.guestId,
            request.roomId,
            request.checkIn,
            request.checkOut
        ).toDto()
    }

    @Transactional
    override fun update(branch: String, reservationId: Int, request: BranchReservationRequest): BranchReservationDto {
        return repo.update(
            branch,
            reservationId,
            request.guestId,
            request.roomId,
            request.checkIn,
            request.checkOut
        )
            .orNotFound("Бронирование с id = $reservationId не найдено")
            .toDto()
    }

    @Transactional
    override fun delete(branch: String, reservationId: Int) {
        val deleted = repo.delete(branch, reservationId)
        if (deleted == 0) throw DatabaseRecordNotFoundException("Бронирование с id = $reservationId не найдено")
    }
}
