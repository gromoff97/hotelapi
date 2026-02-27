package tisbi.gromov.hotelapi.testkit

import org.assertj.core.api.AbstractBigDecimalAssert
import org.assertj.core.api.AbstractBooleanAssert
import org.assertj.core.api.Assertions
import org.assertj.core.api.ObjectArrayAssert
import org.assertj.core.api.ObjectAssert
import org.jooq.Record
import org.springframework.http.ResponseEntity
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
import tisbi.gromov.hotelapi.utils.ApiErrorResponseBody
import java.math.BigDecimal

object CustomAssertions {
    fun assertThat(actual: ResponseEntity<ApiErrorResponseBody>?): ApiErrorResponseEntityAssert =
        ApiErrorResponseEntityAssert(actual)

    fun assertThat(actual: ApiErrorResponseBody?): ApiErrorResponseBodyAssert =
        ApiErrorResponseBodyAssert(actual)

    fun assertThat(actual: Record?): RecordAssert =
        RecordAssert(actual)

    fun <T> assertThat(actual: Array<T>?): ObjectArrayAssert<T> =
        Assertions.assertThat(actual)

    fun assertThat(actual: CentralDepartmentDto?): CentralDepartmentDtoAssert =
        CentralDepartmentDtoAssert(actual)

    fun assertThat(actual: CentralStaffDto?): CentralStaffDtoAssert =
        CentralStaffDtoAssert(actual)

    fun assertThat(actual: CentralSupplierDto?): CentralSupplierDtoAssert =
        CentralSupplierDtoAssert(actual)

    fun assertThat(actual: CentralBranchDto?): CentralBranchDtoAssert =
        CentralBranchDtoAssert(actual)

    fun assertThat(actual: CentralSupplierBranchDto?): CentralSupplierBranchDtoAssert =
        CentralSupplierBranchDtoAssert(actual)

    fun assertThat(actual: BranchGuestDto?): BranchGuestDtoAssert =
        BranchGuestDtoAssert(actual)

    fun assertThat(actual: BranchRoomDto?): BranchRoomDtoAssert =
        BranchRoomDtoAssert(actual)

    fun assertThat(actual: BranchReservationDto?): BranchReservationDtoAssert =
        BranchReservationDtoAssert(actual)

    fun assertThat(actual: BranchStaffDto?): BranchStaffDtoAssert =
        BranchStaffDtoAssert(actual)

    fun assertThat(actual: BranchReservationStaffDto?): BranchReservationStaffDtoAssert =
        BranchReservationStaffDtoAssert(actual)

    fun assertThat(actual: Boolean?): AbstractBooleanAssert<*> =
        Assertions.assertThat(actual)

    fun assertThat(actual: BigDecimal?): AbstractBigDecimalAssert<*> =
        Assertions.assertThat(actual)

    fun <T> assertThat(actual: T?): ObjectAssert<T> =
        Assertions.assertThat(actual)
}
