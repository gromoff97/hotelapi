package tisbi.gromov.hotelapi.utils

import tisbi.gromov.hotelapi.branch.guest.BranchGuestDto
import tisbi.gromov.hotelapi.branch.reservation.BranchReservationDto
import tisbi.gromov.hotelapi.branch.reservation_staff.BranchReservationStaffDto
import tisbi.gromov.hotelapi.branch.room.BranchRoomDto
import tisbi.gromov.hotelapi.branch.staff.BranchStaffDto
import tisbi.gromov.hotelapi.jooq.branch.tables.records.GuestRecord
import tisbi.gromov.hotelapi.jooq.branch.tables.records.ReservationRecord
import tisbi.gromov.hotelapi.jooq.branch.tables.records.ReservationStaffRecord
import tisbi.gromov.hotelapi.jooq.branch.tables.records.RoomRecord
import tisbi.gromov.hotelapi.jooq.branch.tables.records.StaffRecord

fun GuestRecord.toDto(): BranchGuestDto = BranchGuestDto(
    guestId = requireNotNull(this.guestId),
    fullName = requireNotNull(this.fullName),
    phone = this.phone
)

fun RoomRecord.toDto(): BranchRoomDto = BranchRoomDto(
    roomId = requireNotNull(this.roomId),
    number = requireNotNull(this.number),
    type = this.type,
    price = this.price
)

fun StaffRecord.toDto(): BranchStaffDto = BranchStaffDto(
    staffId = requireNotNull(this.staffId),
    name = requireNotNull(this.name),
    role = requireNotNull(this.role)
)

fun ReservationRecord.toDto(): BranchReservationDto = BranchReservationDto(
    reservationId = requireNotNull(this.reservationId),
    guestId = requireNotNull(this.guestId),
    roomId = requireNotNull(this.roomId),
    checkIn = requireNotNull(this.checkIn),
    checkOut = requireNotNull(this.checkOut)
)

fun ReservationStaffRecord.toDto(): BranchReservationStaffDto = BranchReservationStaffDto(
    reservationId = requireNotNull(this.reservationId),
    staffId = requireNotNull(this.staffId)
)
