package tisbi.gromov.hotelapi.testkit

import org.assertj.core.api.AbstractAssert
import org.assertj.core.api.Assertions
import tisbi.gromov.hotelapi.branch.guest.BranchGuestDto
import tisbi.gromov.hotelapi.branch.reservation.BranchReservationDto
import tisbi.gromov.hotelapi.branch.reservation_staff.BranchReservationStaffDto
import tisbi.gromov.hotelapi.branch.room.BranchRoomDto
import tisbi.gromov.hotelapi.branch.staff.BranchStaffDto
import tisbi.gromov.hotelapi.central.branch.CentralBranchDto
import tisbi.gromov.hotelapi.central.department.CentralDepartmentDto
import tisbi.gromov.hotelapi.central.staff.CentralStaffDto
import tisbi.gromov.hotelapi.central.supplier.CentralSupplierDto
import tisbi.gromov.hotelapi.central.supplier_branch.CentralSupplierBranchDto

class CentralDepartmentDtoAssert(actual: CentralDepartmentDto?) :
    AbstractAssert<CentralDepartmentDtoAssert, CentralDepartmentDto>(actual, CentralDepartmentDtoAssert::class.java) {
    fun hasSameValuesAs(expected: CentralDepartmentDto): CentralDepartmentDtoAssert =
        apply {
            isNotNull()
            Assertions.assertThat(actual).isEqualTo(expected)
        }
}

class CentralStaffDtoAssert(actual: CentralStaffDto?) :
    AbstractAssert<CentralStaffDtoAssert, CentralStaffDto>(actual, CentralStaffDtoAssert::class.java) {
    fun hasSameValuesAs(expected: CentralStaffDto): CentralStaffDtoAssert =
        apply {
            isNotNull()
            Assertions.assertThat(actual).isEqualTo(expected)
        }
}

class CentralSupplierDtoAssert(actual: CentralSupplierDto?) :
    AbstractAssert<CentralSupplierDtoAssert, CentralSupplierDto>(actual, CentralSupplierDtoAssert::class.java) {
    fun hasSameValuesAs(expected: CentralSupplierDto): CentralSupplierDtoAssert =
        apply {
            isNotNull()
            Assertions.assertThat(actual).isEqualTo(expected)
        }
}

class CentralBranchDtoAssert(actual: CentralBranchDto?) :
    AbstractAssert<CentralBranchDtoAssert, CentralBranchDto>(actual, CentralBranchDtoAssert::class.java) {
    fun hasSameValuesAs(expected: CentralBranchDto): CentralBranchDtoAssert =
        apply {
            isNotNull()
            Assertions.assertThat(actual).isEqualTo(expected)
        }
}

class CentralSupplierBranchDtoAssert(actual: CentralSupplierBranchDto?) :
    AbstractAssert<CentralSupplierBranchDtoAssert, CentralSupplierBranchDto>(
        actual,
        CentralSupplierBranchDtoAssert::class.java
    ) {
    fun hasSameValuesAs(expected: CentralSupplierBranchDto): CentralSupplierBranchDtoAssert =
        apply {
            isNotNull()
            Assertions.assertThat(actual).isEqualTo(expected)
        }
}

class BranchGuestDtoAssert(actual: BranchGuestDto?) :
    AbstractAssert<BranchGuestDtoAssert, BranchGuestDto>(actual, BranchGuestDtoAssert::class.java) {
    fun hasSameValuesAs(expected: BranchGuestDto): BranchGuestDtoAssert =
        apply {
            isNotNull()
            Assertions.assertThat(actual).isEqualTo(expected)
        }
}

class BranchRoomDtoAssert(actual: BranchRoomDto?) :
    AbstractAssert<BranchRoomDtoAssert, BranchRoomDto>(actual, BranchRoomDtoAssert::class.java) {
    fun hasSameValuesAs(expected: BranchRoomDto): BranchRoomDtoAssert =
        apply {
            isNotNull()
            val actualValue = actual!!
            Assertions.assertThat(actualValue.roomId).isEqualTo(expected.roomId)
            Assertions.assertThat(actualValue.number).isEqualTo(expected.number)
            Assertions.assertThat(actualValue.type).isEqualTo(expected.type)
            val actualPrice = actualValue.price
            val expectedPrice = expected.price
            if (actualPrice == null || expectedPrice == null) {
                Assertions.assertThat(actualPrice).isEqualTo(expectedPrice)
            } else {
                Assertions.assertThat(actualPrice.compareTo(expectedPrice)).isZero
            }
        }
}

class BranchReservationDtoAssert(actual: BranchReservationDto?) :
    AbstractAssert<BranchReservationDtoAssert, BranchReservationDto>(actual, BranchReservationDtoAssert::class.java) {
    fun hasSameValuesAs(expected: BranchReservationDto): BranchReservationDtoAssert =
        apply {
            isNotNull()
            Assertions.assertThat(actual).isEqualTo(expected)
        }
}

class BranchStaffDtoAssert(actual: BranchStaffDto?) :
    AbstractAssert<BranchStaffDtoAssert, BranchStaffDto>(actual, BranchStaffDtoAssert::class.java) {
    fun hasSameValuesAs(expected: BranchStaffDto): BranchStaffDtoAssert =
        apply {
            isNotNull()
            Assertions.assertThat(actual).isEqualTo(expected)
        }
}

class BranchReservationStaffDtoAssert(actual: BranchReservationStaffDto?) :
    AbstractAssert<BranchReservationStaffDtoAssert, BranchReservationStaffDto>(
        actual,
        BranchReservationStaffDtoAssert::class.java
    ) {
    fun hasSameValuesAs(expected: BranchReservationStaffDto): BranchReservationStaffDtoAssert =
        apply {
            isNotNull()
            Assertions.assertThat(actual).isEqualTo(expected)
        }
}
