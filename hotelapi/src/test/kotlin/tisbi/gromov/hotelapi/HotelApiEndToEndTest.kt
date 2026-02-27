package tisbi.gromov.hotelapi

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration
import tisbi.gromov.hotelapi.branch.guest.BranchGuestDto
import tisbi.gromov.hotelapi.branch.guest.BranchGuestRequest
import tisbi.gromov.hotelapi.branch.reservation.BranchReservationDto
import tisbi.gromov.hotelapi.branch.reservation.BranchReservationRequest
import tisbi.gromov.hotelapi.branch.reservation_staff.BranchReservationStaffDto
import tisbi.gromov.hotelapi.branch.room.BranchRoomDto
import tisbi.gromov.hotelapi.branch.room.BranchRoomRequest
import tisbi.gromov.hotelapi.branch.staff.BranchStaffDto
import tisbi.gromov.hotelapi.branch.staff.BranchStaffRequest
import tisbi.gromov.hotelapi.central.department.CentralDepartmentCreateRequest
import tisbi.gromov.hotelapi.central.department.CentralDepartmentDto
import tisbi.gromov.hotelapi.central.department.CentralDepartmentUpdateRequest
import tisbi.gromov.hotelapi.central.staff.CentralStaffCreateRequest
import tisbi.gromov.hotelapi.central.staff.CentralStaffDto
import tisbi.gromov.hotelapi.central.staff.CentralStaffUpdateRequest
import tisbi.gromov.hotelapi.central.supplier.CentralSupplierCreateRequest
import tisbi.gromov.hotelapi.central.supplier.CentralSupplierDto
import tisbi.gromov.hotelapi.central.supplier.CentralSupplierUpdateRequest
import tisbi.gromov.hotelapi.central.supplier_branch.CentralSupplierBranchDto
import tisbi.gromov.hotelapi.central.branch.CentralBranchDto
import tisbi.gromov.hotelapi.jooq.branch.tables.records.GuestRecord
import tisbi.gromov.hotelapi.jooq.branch.tables.records.ReservationRecord
import tisbi.gromov.hotelapi.jooq.branch.tables.records.ReservationStaffRecord
import tisbi.gromov.hotelapi.jooq.branch.tables.records.RoomRecord
import tisbi.gromov.hotelapi.jooq.branch.tables.records.StaffRecord
import tisbi.gromov.hotelapi.jooq.central.tables.records.DepartmentRecord
import tisbi.gromov.hotelapi.jooq.central.tables.records.BranchRecord
import tisbi.gromov.hotelapi.jooq.central.tables.records.StaffCentralRecord
import tisbi.gromov.hotelapi.jooq.central.tables.records.SupplierBranchRecord
import tisbi.gromov.hotelapi.jooq.central.tables.records.SupplierRecord
import tisbi.gromov.hotelapi.testkit.BranchApiClient
import tisbi.gromov.hotelapi.testkit.CentralRestApiClient
import tisbi.gromov.hotelapi.testkit.CustomAssertions.assertThat
import tisbi.gromov.hotelapi.testkit.TestApiClientConfig
import tisbi.gromov.hotelapi.testkit.TestContainersInitializer
import tisbi.gromov.hotelapi.utils.ApiErrorResponseBody
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

import tisbi.gromov.hotelapi.branch.guest.BranchGuestRepository
import tisbi.gromov.hotelapi.branch.room.BranchRoomRepository
import tisbi.gromov.hotelapi.branch.reservation.BranchReservationRepository
import tisbi.gromov.hotelapi.branch.staff.BranchStaffRepository
import tisbi.gromov.hotelapi.branch.reservation_staff.BranchReservationStaffRepository
import tisbi.gromov.hotelapi.central.department.CentralDepartmentRepository
import tisbi.gromov.hotelapi.central.staff.CentralStaffRepository
import tisbi.gromov.hotelapi.central.supplier.CentralSupplierRepository
import tisbi.gromov.hotelapi.central.branch.CentralBranchRepository
import tisbi.gromov.hotelapi.central.supplier_branch.CentralSupplierBranchRepository
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(
    classes = [TestApiClientConfig::class],
    initializers = [TestContainersInitializer::class]
)
class HotelApiEndToEndTest {

    @Autowired
    private lateinit var centralApi: CentralRestApiClient

    @Autowired
    @Qualifier("branch1ApiClient")
    private lateinit var branch1Api: BranchApiClient

    @Autowired
    @Qualifier("branch2ApiClient")
    private lateinit var branch2Api: BranchApiClient

    @Autowired
    private lateinit var centralDepartmentRepository: CentralDepartmentRepository

    @Autowired
    private lateinit var centralStaffRepository: CentralStaffRepository

    @Autowired
    private lateinit var centralSupplierRepository: CentralSupplierRepository

    @Autowired
    private lateinit var centralBranchRepository: CentralBranchRepository

    @Autowired
    private lateinit var centralSupplierBranchRepository: CentralSupplierBranchRepository

    @Autowired
    private lateinit var branchGuestRepository: BranchGuestRepository

    @Autowired
    private lateinit var branchRoomRepository: BranchRoomRepository

    @Autowired
    private lateinit var branchReservationRepository: BranchReservationRepository

    @Autowired
    private lateinit var branchStaffRepository: BranchStaffRepository

    @Autowired
    private lateinit var branchReservationStaffRepository: BranchReservationStaffRepository

    @Test
    fun fullEndToEndScenario() {
        val suffix = UUID.randomUUID().toString().substring(0, 8)
        val branchCode = "branch1"
        val branch2InitialCounts = captureBranchCounts("branch2")

        // -------- Central: department --------
        val departmentResp = centralApi.createDepartment(CentralDepartmentCreateRequest(name = "IT-$suffix"))
        assertThat(departmentResp.statusCode).isEqualTo(HttpStatus.CREATED)
        val actualDepartmentBody = requireNotNull(departmentResp.body)
        val departmentId = actualDepartmentBody.departmentId
        val expectedDepartment = CentralDepartmentDto(departmentId = departmentId, name = "IT-$suffix")
        assertThat(actualDepartmentBody).hasSameValuesAs(expectedDepartment)
        val actualDepartmentRecordCandidate = centralDepartmentRepository.findById(departmentId)
        val actualDepartmentRecord = assertThat(actualDepartmentRecordCandidate).isNotNullValue()
        val expectedDepartmentRecord = DepartmentRecord(departmentId, "IT-$suffix")
        assertThat(actualDepartmentRecord).hasSameValuesAs(expectedDepartmentRecord)

        val departmentListResp = centralApi.listDepartments()
        assertThat(departmentListResp.statusCode).isEqualTo(HttpStatus.OK)
        val departmentListBody = requireNotNull(departmentListResp.body)
        assertThat(departmentListBody).anyMatch { it.departmentId == departmentId }
        val departmentFromList = departmentListBody.first { it.departmentId == departmentId }
        assertThat(departmentFromList).hasSameValuesAs(expectedDepartment)

        val departmentListFilteredResp = centralApi.listDepartments("IT-$suffix")
        assertThat(departmentListFilteredResp.statusCode).isEqualTo(HttpStatus.OK)
        val departmentListFilteredBody = requireNotNull(departmentListFilteredResp.body)
        assertThat(departmentListFilteredBody)
            .isNotEmpty
            .allMatch { it.name.contains("IT-$suffix") }
        val departmentFilteredFromList = departmentListFilteredBody.first { it.departmentId == departmentId }
        assertThat(departmentFilteredFromList).hasSameValuesAs(expectedDepartment)

        val departmentGetResp = centralApi.getDepartment(departmentId)
        assertThat(departmentGetResp.statusCode).isEqualTo(HttpStatus.OK)
        val departmentGetBody = requireNotNull(departmentGetResp.body)
        assertThat(departmentGetBody).hasSameValuesAs(expectedDepartment)

        val departmentUpdateResp = centralApi.updateDepartment(
            departmentId,
            CentralDepartmentUpdateRequest(name = "IT-$suffix-upd")
        )
        assertThat(departmentUpdateResp.statusCode).isEqualTo(HttpStatus.OK)
        val expectedDepartmentUpdated = CentralDepartmentDto(departmentId = departmentId, name = "IT-$suffix-upd")
        val actualDepartmentUpdated = requireNotNull(departmentUpdateResp.body)
        assertThat(actualDepartmentUpdated).hasSameValuesAs(expectedDepartmentUpdated)
        val actualDepartmentUpdatedRecordCandidate = centralDepartmentRepository.findById(departmentId)
        val actualDepartmentUpdatedRecord = assertThat(actualDepartmentUpdatedRecordCandidate).isNotNullValue()
        val expectedDepartmentUpdatedRecord = DepartmentRecord(departmentId, "IT-$suffix-upd")
        assertThat(actualDepartmentUpdatedRecord).hasSameValuesAs(expectedDepartmentUpdatedRecord)

        val departmentListAfterUpdateResp = centralApi.listDepartments("IT-$suffix-upd")
        assertThat(departmentListAfterUpdateResp.statusCode).isEqualTo(HttpStatus.OK)
        val departmentListAfterUpdateBody = requireNotNull(departmentListAfterUpdateResp.body)
        assertThat(departmentListAfterUpdateBody).isNotEmpty
        val departmentAfterUpdateFromList = departmentListAfterUpdateBody.first { it.departmentId == departmentId }
        assertThat(departmentAfterUpdateFromList).hasSameValuesAs(expectedDepartmentUpdated)

        val departmentUpdateMissingResp = centralApi.updateDepartment(
            departmentId + 99999,
            CentralDepartmentUpdateRequest(name = "IT-$suffix-missing"),
            ApiErrorResponseBody::class.java
        )
        assertThat(departmentUpdateMissingResp).correspondsToNotFoundResponse("Департамент с id = ${departmentId + 99999} не найден")

        val departmentDuplicateResp = centralApi.createDepartment(
            CentralDepartmentCreateRequest(name = "IT-$suffix-upd"),
            ApiErrorResponseBody::class.java
        )
        assertThat(departmentDuplicateResp).correspondsToConflictResponse("Запись с такими ключевыми значениями уже существует.")

        // -------- Central: staff --------
        val centralStaffResp = centralApi.createStaff(
            CentralStaffCreateRequest(name = "Charlie-$suffix", departmentId = departmentId)
        )
        assertThat(centralStaffResp.statusCode).isEqualTo(HttpStatus.CREATED)
        val actualCentralStaffBody = requireNotNull(centralStaffResp.body)
        val centralStaffIdValue = actualCentralStaffBody.staffId
        val expectedCentralStaff = CentralStaffDto(staffId = centralStaffIdValue, name = "Charlie-$suffix", departmentId = departmentId)
        assertThat(actualCentralStaffBody).hasSameValuesAs(expectedCentralStaff)
        val actualCentralStaffRecordCandidate = centralStaffRepository.findById(centralStaffIdValue)
        val actualCentralStaffRecord = assertThat(actualCentralStaffRecordCandidate).isNotNullValue()
        val expectedCentralStaffRecord = StaffCentralRecord(centralStaffIdValue, "Charlie-$suffix", departmentId)
        assertThat(actualCentralStaffRecord).hasSameValuesAs(expectedCentralStaffRecord)

        val centralStaffListResp = centralApi.listStaff(departmentId)
        assertThat(centralStaffListResp.statusCode).isEqualTo(HttpStatus.OK)
        val centralStaffListBody = requireNotNull(centralStaffListResp.body)
        assertThat(centralStaffListBody)
            .isNotEmpty
            .anyMatch { it.staffId == centralStaffIdValue }
            .allMatch { it.departmentId == departmentId }
        val centralStaffFromList = centralStaffListBody.first { it.staffId == centralStaffIdValue }
        assertThat(centralStaffFromList).hasSameValuesAs(expectedCentralStaff)

        val centralStaffGetResp = centralApi.getStaff(centralStaffIdValue)
        assertThat(centralStaffGetResp.statusCode).isEqualTo(HttpStatus.OK)
        val centralStaffGetBody = requireNotNull(centralStaffGetResp.body)
        assertThat(centralStaffGetBody).hasSameValuesAs(expectedCentralStaff)

        val centralStaffCreateBadResp = centralApi.createStaff(
            CentralStaffCreateRequest(name = "Charlie-$suffix-bad", departmentId = departmentId + 99999),
            ApiErrorResponseBody::class.java
        )
        assertThat(centralStaffCreateBadResp).correspondsToConflictResponse("Нарушена ссылочная целостность. Проверьте идентификаторы связанных сущностей.")

        val centralStaffUpdateResp = centralApi.updateStaff(
            centralStaffIdValue,
            CentralStaffUpdateRequest(name = "Charlie-$suffix-upd", departmentId = departmentId)
        )
        assertThat(centralStaffUpdateResp.statusCode).isEqualTo(HttpStatus.OK)
        val expectedCentralStaffUpdated = CentralStaffDto(staffId = centralStaffIdValue, name = "Charlie-$suffix-upd", departmentId = departmentId)
        val actualCentralStaffUpdatedBody = requireNotNull(centralStaffUpdateResp.body)
        assertThat(actualCentralStaffUpdatedBody).hasSameValuesAs(expectedCentralStaffUpdated)
        val actualCentralStaffUpdatedRecordCandidate = centralStaffRepository.findById(centralStaffIdValue)
        val actualCentralStaffUpdatedRecord = assertThat(actualCentralStaffUpdatedRecordCandidate).isNotNullValue()
        val expectedCentralStaffUpdatedRecord = StaffCentralRecord(centralStaffIdValue, "Charlie-$suffix-upd", departmentId)
        assertThat(actualCentralStaffUpdatedRecord).hasSameValuesAs(expectedCentralStaffUpdatedRecord)

        val centralStaffGetUpdatedResp = centralApi.getStaff(centralStaffIdValue)
        assertThat(centralStaffGetUpdatedResp.statusCode).isEqualTo(HttpStatus.OK)
        val centralStaffGetUpdatedBody = requireNotNull(centralStaffGetUpdatedResp.body)
        assertThat(centralStaffGetUpdatedBody).hasSameValuesAs(expectedCentralStaffUpdated)

        val centralStaffListAfterUpdateResp = centralApi.listStaff(departmentId)
        assertThat(centralStaffListAfterUpdateResp.statusCode).isEqualTo(HttpStatus.OK)
        val centralStaffListAfterUpdateBody = requireNotNull(centralStaffListAfterUpdateResp.body)
        assertThat(centralStaffListAfterUpdateBody).isNotEmpty
        val centralStaffAfterUpdateFromList = centralStaffListAfterUpdateBody.first { it.staffId == centralStaffIdValue }
        assertThat(centralStaffAfterUpdateFromList).hasSameValuesAs(expectedCentralStaffUpdated)

        val centralStaffUpdateMissingResp = centralApi.updateStaff(
            centralStaffIdValue + 99999,
            CentralStaffUpdateRequest(name = "Charlie-$suffix-missing", departmentId = departmentId),
            ApiErrorResponseBody::class.java
        )
        assertThat(centralStaffUpdateMissingResp).correspondsToNotFoundResponse("Сотрудник центрального узла с id = ${centralStaffIdValue + 99999} не найден")

        // -------- Central: supplier --------
        val supplierResp = centralApi.createSupplier(CentralSupplierCreateRequest(companyName = "Supplier-$suffix"))
        assertThat(supplierResp.statusCode).isEqualTo(HttpStatus.CREATED)
        val actualSupplierBody = requireNotNull(supplierResp.body)
        val supplierId = actualSupplierBody.supplierId
        val expectedSupplier = CentralSupplierDto(supplierId = supplierId, companyName = "Supplier-$suffix")
        assertThat(actualSupplierBody).hasSameValuesAs(expectedSupplier)
        val actualSupplierRecordCandidate = centralSupplierRepository.findById(supplierId)
        val actualSupplierRecord = assertThat(actualSupplierRecordCandidate).isNotNullValue()
        val expectedSupplierRecord = SupplierRecord(supplierId, "Supplier-$suffix")
        assertThat(actualSupplierRecord).hasSameValuesAs(expectedSupplierRecord)

        val supplierListResp = centralApi.listSuppliers("Supplier-$suffix")
        assertThat(supplierListResp.statusCode).isEqualTo(HttpStatus.OK)
        val supplierListBody = requireNotNull(supplierListResp.body)
        assertThat(supplierListBody)
            .isNotEmpty
            .anyMatch { it.supplierId == supplierId }
            .allMatch { it.companyName.contains("Supplier-$suffix") }
        val supplierFromList = supplierListBody.first { it.supplierId == supplierId }
        assertThat(supplierFromList).hasSameValuesAs(expectedSupplier)

        val supplierGetResp = centralApi.getSupplier(supplierId)
        assertThat(supplierGetResp.statusCode).isEqualTo(HttpStatus.OK)
        val supplierGetBody = requireNotNull(supplierGetResp.body)
        assertThat(supplierGetBody).hasSameValuesAs(expectedSupplier)

        val supplierDuplicateResp = centralApi.createSupplier(
            CentralSupplierCreateRequest(companyName = "Supplier-$suffix"),
            ApiErrorResponseBody::class.java
        )
        assertThat(supplierDuplicateResp).correspondsToConflictResponse("Запись с такими ключевыми значениями уже существует.")

        val supplierUpdateResp = centralApi.updateSupplier(
            supplierId,
            CentralSupplierUpdateRequest(companyName = "Supplier-$suffix-upd")
        )
        assertThat(supplierUpdateResp.statusCode).isEqualTo(HttpStatus.OK)
        val expectedSupplierUpdated = CentralSupplierDto(supplierId = supplierId, companyName = "Supplier-$suffix-upd")
        val actualSupplierUpdatedBody = requireNotNull(supplierUpdateResp.body)
        assertThat(actualSupplierUpdatedBody).hasSameValuesAs(expectedSupplierUpdated)
        val actualSupplierUpdatedRecordCandidate = centralSupplierRepository.findById(supplierId)
        val actualSupplierUpdatedRecord = assertThat(actualSupplierUpdatedRecordCandidate).isNotNullValue()
        val expectedSupplierUpdatedRecord = SupplierRecord(supplierId, "Supplier-$suffix-upd")
        assertThat(actualSupplierUpdatedRecord).hasSameValuesAs(expectedSupplierUpdatedRecord)

        val supplierGetUpdatedResp = centralApi.getSupplier(supplierId)
        assertThat(supplierGetUpdatedResp.statusCode).isEqualTo(HttpStatus.OK)
        val supplierGetUpdatedBody = requireNotNull(supplierGetUpdatedResp.body)
        assertThat(supplierGetUpdatedBody).hasSameValuesAs(expectedSupplierUpdated)

        val supplierListAfterUpdateResp = centralApi.listSuppliers("Supplier-$suffix-upd")
        assertThat(supplierListAfterUpdateResp.statusCode).isEqualTo(HttpStatus.OK)
        val supplierListAfterUpdateBody = requireNotNull(supplierListAfterUpdateResp.body)
        assertThat(supplierListAfterUpdateBody).isNotEmpty
        val supplierAfterUpdateFromList = supplierListAfterUpdateBody.first { it.supplierId == supplierId }
        assertThat(supplierAfterUpdateFromList).hasSameValuesAs(expectedSupplierUpdated)

        val supplierUpdateMissingResp = centralApi.updateSupplier(
            supplierId + 99999,
            CentralSupplierUpdateRequest(companyName = "Supplier-$suffix-missing"),
            ApiErrorResponseBody::class.java
        )
        assertThat(supplierUpdateMissingResp).correspondsToNotFoundResponse("Поставщик с id = ${supplierId + 99999} не найден")

        val branchesResp = centralApi.listBranches()
        assertThat(branchesResp.statusCode).isEqualTo(HttpStatus.OK)
        val actualBranches = requireNotNull(branchesResp.body)
        assertThat(actualBranches).isNotEmpty
        val branchId = actualBranches[0].branchId
        val actualBranchRecordCandidate = centralBranchRepository.findById(branchId)
        val actualBranchRecord = assertThat(actualBranchRecordCandidate).isNotNullValue() as BranchRecord

        val branchGetResp = centralApi.getBranch(branchId)
        assertThat(branchGetResp.statusCode).isEqualTo(HttpStatus.OK)
        val expectedBranch = CentralBranchDto(branchId = branchId, name = actualBranchRecord.name, address = actualBranchRecord.address)
        val branchFromList = actualBranches.first { it.branchId == branchId }
        assertThat(branchFromList).hasSameValuesAs(expectedBranch)
        val actualBranchBody = requireNotNull(branchGetResp.body)
        assertThat(actualBranchBody).hasSameValuesAs(expectedBranch)

        val branchGetMissingResp = centralApi.getBranch(branchId + 99999, ApiErrorResponseBody::class.java)
        assertThat(branchGetMissingResp).correspondsToNotFoundResponse("Филиал с id = ${branchId + 99999} не найден")

        val linkResp = centralApi.linkSupplierBranch(supplierId, branchId)
        assertThat(linkResp.statusCode).isEqualTo(HttpStatus.CREATED)
        val expectedSupplierBranch = CentralSupplierBranchDto(supplierId = supplierId, branchId = branchId)
        val actualSupplierBranchBody = requireNotNull(linkResp.body)
        assertThat(actualSupplierBranchBody).hasSameValuesAs(expectedSupplierBranch)
        val actualSupplierBranchRecordCandidate = centralSupplierBranchRepository.findById(supplierId, branchId)
        val actualSupplierBranchRecord = assertThat(actualSupplierBranchRecordCandidate).isNotNullValue()
        val expectedSupplierBranchRecord = SupplierBranchRecord(supplierId, branchId)
        assertThat(actualSupplierBranchRecord).hasSameValuesAs(expectedSupplierBranchRecord)

        val duplicateSupplierBranchResp = centralApi.linkSupplierBranch(supplierId, branchId, ApiErrorResponseBody::class.java)
        assertThat(duplicateSupplierBranchResp).correspondsToConflictResponse("Запись с такими ключевыми значениями уже существует.")

        val linkedBranchesResp = centralApi.listSupplierBranches(supplierId)
        assertThat(linkedBranchesResp.statusCode).isEqualTo(HttpStatus.OK)
        val actualLinkedBranches = requireNotNull(linkedBranchesResp.body)
        assertThat(actualLinkedBranches).anyMatch { it.branchId == branchId }
        val linkedBranchFromList = actualLinkedBranches.first { it.branchId == branchId }
        assertThat(linkedBranchFromList).hasSameValuesAs(expectedSupplierBranch)

        cleanupCentral(
            supplierId = supplierId,
            branchId = branchId,
            staffId = centralStaffIdValue,
            departmentId = departmentId
        )

        // -------- Branch (branch1): guest --------

        val guestResp = branch1Api.createGuest(
            BranchGuestRequest(fullName = "Guest $suffix", phone = "+7 999 111-11-11")
        )
        assertThat(guestResp.statusCode).isEqualTo(HttpStatus.CREATED)
        val actualGuestBody = requireNotNull(guestResp.body)
        val guestId = actualGuestBody.guestId
        val expectedGuest = BranchGuestDto(guestId = guestId, fullName = "Guest $suffix", phone = "+7 999 111-11-11")
        assertThat(actualGuestBody).hasSameValuesAs(expectedGuest)
        val actualGuestRecordCandidate = branchGuestRepository.findById(branchCode, guestId)
        val actualGuestRecord = assertThat(actualGuestRecordCandidate).isNotNullValue()
        val expectedGuestRecord = GuestRecord(guestId, "Guest $suffix", "+7 999 111-11-11")
        assertThat(actualGuestRecord).hasSameValuesAs(expectedGuestRecord)

        val guestListResp = branch1Api.listGuests()
        assertThat(guestListResp.statusCode).isEqualTo(HttpStatus.OK)
        val guestListBody = requireNotNull(guestListResp.body)
        assertThat(guestListBody)
            .isNotEmpty
            .anyMatch { it.guestId == guestId }
        val guestFromList = guestListBody.first { it.guestId == guestId }
        assertThat(guestFromList).hasSameValuesAs(expectedGuest)

        val guestGetResp = branch1Api.getGuest(guestId)
        assertThat(guestGetResp.statusCode).isEqualTo(HttpStatus.OK)
        val actualGuestGet = requireNotNull(guestGetResp.body)
        assertThat(actualGuestGet).hasSameValuesAs(expectedGuest)

        val guestUpdateResp = branch1Api.updateGuest(
            guestId,
            BranchGuestRequest(fullName = "Guest $suffix Updated", phone = null)
        )
        assertThat(guestUpdateResp.statusCode).isEqualTo(HttpStatus.OK)
        val expectedGuestUpdated = BranchGuestDto(guestId = guestId, fullName = "Guest $suffix Updated", phone = null)
        val actualGuestUpdated = requireNotNull(guestUpdateResp.body)
        assertThat(actualGuestUpdated).hasSameValuesAs(expectedGuestUpdated)
        val actualGuestUpdatedRecordCandidate = branchGuestRepository.findById(branchCode, guestId)
        val actualGuestUpdatedRecord = assertThat(actualGuestUpdatedRecordCandidate).isNotNullValue()
        val expectedGuestUpdatedRecord = GuestRecord(guestId, "Guest $suffix Updated", null)
        assertThat(actualGuestUpdatedRecord).hasSameValuesAs(expectedGuestUpdatedRecord)

        val guestListAfterUpdateResp = branch1Api.listGuests()
        assertThat(guestListAfterUpdateResp.statusCode).isEqualTo(HttpStatus.OK)
        val guestListAfterUpdateBody = requireNotNull(guestListAfterUpdateResp.body)
        assertThat(guestListAfterUpdateBody).isNotEmpty
        val guestAfterUpdateFromList = guestListAfterUpdateBody.first { it.guestId == guestId }
        assertThat(guestAfterUpdateFromList).hasSameValuesAs(expectedGuestUpdated)

        val guestUpdateMissingResp = branch1Api.updateGuest(
            guestId + 99999,
            BranchGuestRequest(fullName = "Guest $suffix Missing", phone = null),
            ApiErrorResponseBody::class.java
        )
        assertThat(guestUpdateMissingResp).correspondsToNotFoundResponse("Гость с id = ${guestId + 99999} не найден")

        // -------- Branch (branch1): room --------
        val roomResp = branch1Api.createRoom(
            BranchRoomRequest(number = "9$guestId", type = "double", price = BigDecimal("5000"))
        )
        assertThat(roomResp.statusCode).isEqualTo(HttpStatus.CREATED)
        val actualRoomBody = requireNotNull(roomResp.body)
        val roomId = actualRoomBody.roomId
        val expectedRoom = BranchRoomDto(roomId = roomId, number = "9$guestId", type = "double", price = BigDecimal("5000"))
        assertThat(actualRoomBody).hasSameValuesAs(expectedRoom)
        val actualRoomRecordCandidate = branchRoomRepository.findById(branchCode, roomId)
        val actualRoomRecord = assertThat(actualRoomRecordCandidate).isNotNullValue()
        val expectedRoomRecord = RoomRecord(roomId, "9$guestId", "double", BigDecimal("5000"))
        assertThat(actualRoomRecord).hasSameValuesAs(expectedRoomRecord)

        val roomListResp = branch1Api.listRooms()
        assertThat(roomListResp.statusCode).isEqualTo(HttpStatus.OK)
        val roomListBody = requireNotNull(roomListResp.body)
        assertThat(roomListBody)
            .isNotEmpty
            .anyMatch { it.roomId == roomId }
        val roomFromList = roomListBody.first { it.roomId == roomId }
        assertThat(roomFromList).hasSameValuesAs(expectedRoom)

        val roomListTypeResp = branch1Api.listRooms(type = "double")
        assertThat(roomListTypeResp.statusCode).isEqualTo(HttpStatus.OK)
        val roomListTypeBody = requireNotNull(roomListTypeResp.body)
        assertThat(roomListTypeBody)
            .isNotEmpty
            .allMatch { it.type == "double" }
        val roomFromTypeList = roomListTypeBody.first { it.roomId == roomId }
        assertThat(roomFromTypeList).hasSameValuesAs(expectedRoom)

        val roomListPriceResp = branch1Api.listRooms(maxPrice = BigDecimal("7000"))
        assertThat(roomListPriceResp.statusCode).isEqualTo(HttpStatus.OK)
        val roomListPriceBody = requireNotNull(roomListPriceResp.body)
        assertThat(roomListPriceBody)
            .isNotEmpty
            .allMatch { room ->
                val price = room.price
                price == null || price.compareTo(BigDecimal("7000")) <= 0
            }
        val roomFromPriceList = roomListPriceBody.first { it.roomId == roomId }
        assertThat(roomFromPriceList).hasSameValuesAs(expectedRoom)

        val roomGetResp = branch1Api.getRoom(roomId)
        assertThat(roomGetResp.statusCode).isEqualTo(HttpStatus.OK)
        val roomGetBody = requireNotNull(roomGetResp.body)
        assertThat(roomGetBody).hasSameValuesAs(expectedRoom)

        val roomUpdateResp = branch1Api.updateRoom(
            roomId,
            BranchRoomRequest(number = "9$guestId", type = "suite", price = BigDecimal("8000"))
        )
        assertThat(roomUpdateResp.statusCode).isEqualTo(HttpStatus.OK)
        val expectedRoomUpdated = BranchRoomDto(roomId = roomId, number = "9$guestId", type = "suite", price = BigDecimal("8000"))
        val actualRoomUpdated = requireNotNull(roomUpdateResp.body)
        assertThat(actualRoomUpdated).hasSameValuesAs(expectedRoomUpdated)
        val actualRoomUpdatedRecordCandidate = branchRoomRepository.findById(branchCode, roomId)
        val actualRoomUpdatedRecord = assertThat(actualRoomUpdatedRecordCandidate).isNotNullValue()
        val expectedRoomUpdatedRecord = RoomRecord(roomId, "9$guestId", "suite", BigDecimal("8000"))
        assertThat(actualRoomUpdatedRecord).hasSameValuesAs(expectedRoomUpdatedRecord)

        val roomListTypeUpdatedResp = branch1Api.listRooms(type = "suite")
        assertThat(roomListTypeUpdatedResp.statusCode).isEqualTo(HttpStatus.OK)
        val roomListTypeUpdatedBody = requireNotNull(roomListTypeUpdatedResp.body)
        assertThat(roomListTypeUpdatedBody)
            .isNotEmpty
            .anyMatch { it.roomId == roomId }
        val roomUpdatedFromTypeList = roomListTypeUpdatedBody.first { it.roomId == roomId }
        assertThat(roomUpdatedFromTypeList).hasSameValuesAs(expectedRoomUpdated)

        val roomUpdateMissingResp = branch1Api.updateRoom(
            roomId + 99999,
            BranchRoomRequest(number = "missing-$roomId", type = "single", price = BigDecimal("1000")),
            ApiErrorResponseBody::class.java
        )
        assertThat(roomUpdateMissingResp).correspondsToNotFoundResponse("Номер с id = ${roomId + 99999} не найден")

        // Trigger: negative price (expect 409)
        val badRoomResp = branch1Api.createRoom(
            BranchRoomRequest(number = "bad-$suffix", type = "single", price = BigDecimal("-1")),
            ApiErrorResponseBody::class.java
        )
        assertThat(badRoomResp).correspondsToConflictResponse("Цена номера не может быть отрицательной.")

        // -------- Branch (branch1): reservation --------
        val checkIn = LocalDate.now().plusDays(1)
        val checkOut = checkIn.plusDays(2)

        // Trigger: invalid dates (expect 409)
        val badReservationResp = branch1Api.createReservation(
            BranchReservationRequest(guestId, roomId, LocalDate.now().plusDays(10), LocalDate.now().plusDays(5)),
            ApiErrorResponseBody::class.java
        )
        assertThat(badReservationResp).correspondsToConflictResponse("Дата заезда должна быть меньше или равна дате выезда.")

        val reservationResp = branch1Api.createReservation(
            BranchReservationRequest(guestId, roomId, checkIn, checkOut)
        )
        assertThat(reservationResp.statusCode).isEqualTo(HttpStatus.CREATED)
        val actualReservationBody = requireNotNull(reservationResp.body)
        val reservationId = actualReservationBody.reservationId
        val expectedReservation = BranchReservationDto(reservationId = reservationId, guestId = guestId, roomId = roomId, checkIn = checkIn, checkOut = checkOut)
        assertThat(actualReservationBody).hasSameValuesAs(expectedReservation)
        val actualReservationRecordCandidate = branchReservationRepository.findById(branchCode, reservationId)
        val actualReservationRecord = assertThat(actualReservationRecordCandidate).isNotNullValue()
        val expectedReservationRecord = ReservationRecord(reservationId, guestId, roomId, checkIn, checkOut)
        assertThat(actualReservationRecord).hasSameValuesAs(expectedReservationRecord)

        val reservationGetResp = branch1Api.getReservation(reservationId)
        assertThat(reservationGetResp.statusCode).isEqualTo(HttpStatus.OK)
        val reservationGetBody = requireNotNull(reservationGetResp.body)
        assertThat(reservationGetBody).hasSameValuesAs(expectedReservation)

        val updatedCheckIn = checkIn.plusDays(1)
        val updatedCheckOut = checkOut.plusDays(1)
        val reservationUpdateResp = branch1Api.updateReservation(
            reservationId,
            BranchReservationRequest(guestId, roomId, updatedCheckIn, updatedCheckOut)
        )
        assertThat(reservationUpdateResp.statusCode).isEqualTo(HttpStatus.OK)
        val expectedReservationUpdated = BranchReservationDto(reservationId = reservationId, guestId = guestId, roomId = roomId, checkIn = updatedCheckIn, checkOut = updatedCheckOut)
        val actualReservationUpdated = requireNotNull(reservationUpdateResp.body)
        assertThat(actualReservationUpdated).hasSameValuesAs(expectedReservationUpdated)
        val actualReservationUpdatedRecordCandidate = branchReservationRepository.findById(branchCode, reservationId)
        val actualReservationUpdatedRecord = assertThat(actualReservationUpdatedRecordCandidate).isNotNullValue()
        val expectedReservationUpdatedRecord = ReservationRecord(reservationId, guestId, roomId, updatedCheckIn, updatedCheckOut)
        assertThat(actualReservationUpdatedRecord).hasSameValuesAs(expectedReservationUpdatedRecord)

        val reservationGetUpdatedResp = branch1Api.getReservation(reservationId)
        assertThat(reservationGetUpdatedResp.statusCode).isEqualTo(HttpStatus.OK)
        val reservationGetUpdatedBody = requireNotNull(reservationGetUpdatedResp.body)
        assertThat(reservationGetUpdatedBody).hasSameValuesAs(expectedReservationUpdated)

        val reservationUpdateMissingResp = branch1Api.updateReservation(
            reservationId + 99999,
            BranchReservationRequest(guestId, roomId, updatedCheckIn, updatedCheckOut),
            ApiErrorResponseBody::class.java
        )
        assertThat(reservationUpdateMissingResp).correspondsToNotFoundResponse("Бронирование с id = ${reservationId + 99999} не найдено")

        val reservationListAllResp = branch1Api.listReservations()
        assertThat(reservationListAllResp.statusCode).isEqualTo(HttpStatus.OK)
        val reservationListAllBody = requireNotNull(reservationListAllResp.body)
        assertThat(reservationListAllBody)
            .isNotEmpty
            .anyMatch { it.reservationId == reservationId }
        val reservationFromListAll = reservationListAllBody.first { it.reservationId == reservationId }
        assertThat(reservationFromListAll).hasSameValuesAs(expectedReservationUpdated)

        val reservationListByGuestResp = branch1Api.listReservations(guestId = guestId)
        assertThat(reservationListByGuestResp.statusCode).isEqualTo(HttpStatus.OK)
        val reservationListByGuestBody = requireNotNull(reservationListByGuestResp.body)
        assertThat(reservationListByGuestBody)
            .isNotEmpty
            .allMatch { it.guestId == guestId }
        val reservationFromGuestList = reservationListByGuestBody.first { it.reservationId == reservationId }
        assertThat(reservationFromGuestList).hasSameValuesAs(expectedReservationUpdated)

        val reservationListByRoomResp = branch1Api.listReservations(roomId = roomId)
        assertThat(reservationListByRoomResp.statusCode).isEqualTo(HttpStatus.OK)
        val reservationListByRoomBody = requireNotNull(reservationListByRoomResp.body)
        assertThat(reservationListByRoomBody)
            .isNotEmpty
            .allMatch { it.roomId == roomId }
        val reservationFromRoomList = reservationListByRoomBody.first { it.reservationId == reservationId }
        assertThat(reservationFromRoomList).hasSameValuesAs(expectedReservationUpdated)

        val reservationListByDateResp = branch1Api.listReservations(from = updatedCheckIn, to = updatedCheckIn)
        assertThat(reservationListByDateResp.statusCode).isEqualTo(HttpStatus.OK)
        val reservationListByDateBody = requireNotNull(reservationListByDateResp.body)
        assertThat(reservationListByDateBody)
            .isNotEmpty
            .allMatch { reservation ->
                !reservation.checkIn.isBefore(updatedCheckIn) && !reservation.checkIn.isAfter(updatedCheckIn)
            }
        val reservationFromDateList = reservationListByDateBody.first { it.reservationId == reservationId }
        assertThat(reservationFromDateList).hasSameValuesAs(expectedReservationUpdated)

        // -------- Branch (branch1): staff + reservation_staff --------
        val staffBranchResp = branch1Api.createStaff(BranchStaffRequest(name = "Staff $suffix", role = "receptionist"))
        assertThat(staffBranchResp.statusCode).isEqualTo(HttpStatus.CREATED)
        val actualBranchStaffBody = requireNotNull(staffBranchResp.body)
        val branchStaffId = actualBranchStaffBody.staffId
        val expectedBranchStaff = BranchStaffDto(staffId = branchStaffId, name = "Staff $suffix", role = "receptionist")
        assertThat(actualBranchStaffBody).hasSameValuesAs(expectedBranchStaff)
        val actualBranchStaffRecordCandidate = branchStaffRepository.findById(branchCode, branchStaffId)
        val actualBranchStaffRecord = assertThat(actualBranchStaffRecordCandidate).isNotNullValue()
        val expectedBranchStaffRecord = StaffRecord(branchStaffId, "Staff $suffix", "receptionist")
        assertThat(actualBranchStaffRecord).hasSameValuesAs(expectedBranchStaffRecord)

        val staffListResp = branch1Api.listStaff()
        assertThat(staffListResp.statusCode).isEqualTo(HttpStatus.OK)
        val staffListBody = requireNotNull(staffListResp.body)
        assertThat(staffListBody)
            .isNotEmpty
            .anyMatch { it.staffId == branchStaffId }

        val staffGetResp = branch1Api.getStaff(branchStaffId)
        assertThat(staffGetResp.statusCode).isEqualTo(HttpStatus.OK)
        val staffGetBody = requireNotNull(staffGetResp.body)
        assertThat(staffGetBody).hasSameValuesAs(expectedBranchStaff)

        val staffUpdateResp = branch1Api.updateStaff(
            branchStaffId,
            BranchStaffRequest(name = "Staff $suffix Updated", role = "manager")
        )
        assertThat(staffUpdateResp.statusCode).isEqualTo(HttpStatus.OK)
        val expectedBranchStaffUpdated = BranchStaffDto(staffId = branchStaffId, name = "Staff $suffix Updated", role = "manager")
        val staffUpdateBody = requireNotNull(staffUpdateResp.body)
        assertThat(staffUpdateBody).hasSameValuesAs(expectedBranchStaffUpdated)
        val actualBranchStaffUpdatedRecordCandidate = branchStaffRepository.findById(branchCode, branchStaffId)
        val actualBranchStaffUpdatedRecord = assertThat(actualBranchStaffUpdatedRecordCandidate).isNotNullValue()
        val expectedBranchStaffUpdatedRecord = StaffRecord(branchStaffId, "Staff $suffix Updated", "manager")
        assertThat(actualBranchStaffUpdatedRecord).hasSameValuesAs(expectedBranchStaffUpdatedRecord)

        val staffListAfterUpdateResp = branch1Api.listStaff()
        assertThat(staffListAfterUpdateResp.statusCode).isEqualTo(HttpStatus.OK)
        val staffListAfterUpdateBody = requireNotNull(staffListAfterUpdateResp.body)
        assertThat(staffListAfterUpdateBody).isNotEmpty
        val staffAfterUpdateFromList = staffListAfterUpdateBody.first { it.staffId == branchStaffId }
        assertThat(staffAfterUpdateFromList).hasSameValuesAs(expectedBranchStaffUpdated)

        val staffUpdateMissingResp = branch1Api.updateStaff(
            branchStaffId + 99999,
            BranchStaffRequest(name = "Staff Missing", role = "manager"),
            ApiErrorResponseBody::class.java
        )
        assertThat(staffUpdateMissingResp).correspondsToNotFoundResponse("Сотрудник с id = ${branchStaffId + 99999} не найден")

        val expectedReservationStaff = BranchReservationStaffDto(reservationId = reservationId, staffId = branchStaffId)
        val linkStaffResp = branch1Api.linkStaff(reservationId, branchStaffId)
        assertThat(linkStaffResp.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(linkStaffResp.body).isNull()
        val actualReservationStaffRecordCandidate = branchReservationStaffRepository.findById(branchCode, reservationId, branchStaffId)
        val actualReservationStaffRecord = assertThat(actualReservationStaffRecordCandidate).isNotNullValue()
        val expectedReservationStaffRecord = ReservationStaffRecord(reservationId, branchStaffId)
        assertThat(actualReservationStaffRecord).hasSameValuesAs(expectedReservationStaffRecord)

        val reservationStaffResp = branch1Api.listReservationStaff(reservationId)
        assertThat(reservationStaffResp.statusCode).isEqualTo(HttpStatus.OK)
        val actualReservationStaffList = requireNotNull(reservationStaffResp.body)
        assertThat(actualReservationStaffList).isNotEmpty
        val actualReservationStaffFromList = actualReservationStaffList.first { it.staffId == branchStaffId }
        assertThat(actualReservationStaffFromList).hasSameValuesAs(expectedReservationStaff)

        val branch2CountsAfterBranch1 = captureBranchCounts("branch2")
        assertThat(branch2CountsAfterBranch1).isEqualTo(branch2InitialCounts)

        cleanupBranch(
            branchApi = branch1Api,
            branchCode = "branch1",
            guestId = guestId,
            roomId = roomId,
            reservationId = reservationId,
            staffId = branchStaffId
        )
        val branch1CountsBeforeBranch2 = captureBranchCounts(branchCode)

        // -------- Branch (branch2): guest --------
        val branch2Code = "branch2"
        val suffix2 = "${suffix}-b2"

        val guest2Resp = branch2Api.createGuest(
            BranchGuestRequest(fullName = "Guest $suffix2", phone = "+7 999 222-22-22")
        )
        assertThat(guest2Resp.statusCode).isEqualTo(HttpStatus.CREATED)
        val guest2Body = requireNotNull(guest2Resp.body)
        val guest2Id = guest2Body.guestId
        val expectedGuest2 = BranchGuestDto(guestId = guest2Id, fullName = "Guest $suffix2", phone = "+7 999 222-22-22")
        assertThat(guest2Body).hasSameValuesAs(expectedGuest2)
        val guest2RecordCandidate = branchGuestRepository.findById(branch2Code, guest2Id)
        val guest2Record = assertThat(guest2RecordCandidate).isNotNullValue()
        val expectedGuest2Record = GuestRecord(guest2Id, "Guest $suffix2", "+7 999 222-22-22")
        assertThat(guest2Record).hasSameValuesAs(expectedGuest2Record)

        val guest2ListResp = branch2Api.listGuests()
        assertThat(guest2ListResp.statusCode).isEqualTo(HttpStatus.OK)
        val guest2ListBody = requireNotNull(guest2ListResp.body)
        assertThat(guest2ListBody)
            .isNotEmpty
            .anyMatch { it.guestId == guest2Id }
        val guest2FromList = guest2ListBody.first { it.guestId == guest2Id }
        assertThat(guest2FromList).hasSameValuesAs(expectedGuest2)

        val guest2GetResp = branch2Api.getGuest(guest2Id)
        assertThat(guest2GetResp.statusCode).isEqualTo(HttpStatus.OK)
        val guest2GetBody = requireNotNull(guest2GetResp.body)
        assertThat(guest2GetBody).hasSameValuesAs(expectedGuest2)

        val guest2UpdateResp = branch2Api.updateGuest(
            guest2Id,
            BranchGuestRequest(fullName = "Guest $suffix2 Updated", phone = null)
        )
        assertThat(guest2UpdateResp.statusCode).isEqualTo(HttpStatus.OK)
        val expectedGuest2Updated = BranchGuestDto(guestId = guest2Id, fullName = "Guest $suffix2 Updated", phone = null)
        val guest2UpdateBody = requireNotNull(guest2UpdateResp.body)
        assertThat(guest2UpdateBody).hasSameValuesAs(expectedGuest2Updated)
        val guest2UpdatedRecordCandidate = branchGuestRepository.findById(branch2Code, guest2Id)
        val guest2UpdatedRecord = assertThat(guest2UpdatedRecordCandidate).isNotNullValue()
        val expectedGuest2UpdatedRecord = GuestRecord(guest2Id, "Guest $suffix2 Updated", null)
        assertThat(guest2UpdatedRecord).hasSameValuesAs(expectedGuest2UpdatedRecord)

        val guest2ListAfterUpdateResp = branch2Api.listGuests()
        assertThat(guest2ListAfterUpdateResp.statusCode).isEqualTo(HttpStatus.OK)
        val guest2ListAfterUpdateBody = requireNotNull(guest2ListAfterUpdateResp.body)
        assertThat(guest2ListAfterUpdateBody).isNotEmpty
        val guest2AfterUpdateFromList = guest2ListAfterUpdateBody.first { it.guestId == guest2Id }
        assertThat(guest2AfterUpdateFromList).hasSameValuesAs(expectedGuest2Updated)

        val guest2UpdateMissingResp = branch2Api.updateGuest(
            guest2Id + 99999,
            BranchGuestRequest(fullName = "Guest $suffix2 Missing", phone = null),
            ApiErrorResponseBody::class.java
        )
        assertThat(guest2UpdateMissingResp).correspondsToNotFoundResponse("Гость с id = ${guest2Id + 99999} не найден")

        // -------- Branch (branch2): room --------
        val room2Resp = branch2Api.createRoom(
            BranchRoomRequest(number = "B2-$guest2Id", type = "double", price = BigDecimal("6000"))
        )
        assertThat(room2Resp.statusCode).isEqualTo(HttpStatus.CREATED)
        val room2Body = requireNotNull(room2Resp.body)
        val room2Id = room2Body.roomId
        val expectedRoom2 = BranchRoomDto(roomId = room2Id, number = "B2-$guest2Id", type = "double", price = BigDecimal("6000"))
        assertThat(room2Body).hasSameValuesAs(expectedRoom2)
        val room2RecordCandidate = branchRoomRepository.findById(branch2Code, room2Id)
        val room2Record = assertThat(room2RecordCandidate).isNotNullValue()
        val expectedRoom2Record = RoomRecord(room2Id, "B2-$guest2Id", "double", BigDecimal("6000"))
        assertThat(room2Record).hasSameValuesAs(expectedRoom2Record)

        val room2ListResp = branch2Api.listRooms()
        assertThat(room2ListResp.statusCode).isEqualTo(HttpStatus.OK)
        val room2ListBody = requireNotNull(room2ListResp.body)
        assertThat(room2ListBody)
            .isNotEmpty
            .anyMatch { it.roomId == room2Id }
        val room2FromList = room2ListBody.first { it.roomId == room2Id }
        assertThat(room2FromList).hasSameValuesAs(expectedRoom2)

        val room2ListTypeResp = branch2Api.listRooms(type = "double")
        assertThat(room2ListTypeResp.statusCode).isEqualTo(HttpStatus.OK)
        val room2ListTypeBody = requireNotNull(room2ListTypeResp.body)
        assertThat(room2ListTypeBody)
            .isNotEmpty
            .allMatch { it.type == "double" }
        val room2FromTypeList = room2ListTypeBody.first { it.roomId == room2Id }
        assertThat(room2FromTypeList).hasSameValuesAs(expectedRoom2)

        val room2ListPriceResp = branch2Api.listRooms(maxPrice = BigDecimal("7000"))
        assertThat(room2ListPriceResp.statusCode).isEqualTo(HttpStatus.OK)
        val room2ListPriceBody = requireNotNull(room2ListPriceResp.body)
        assertThat(room2ListPriceBody)
            .isNotEmpty
            .allMatch { room ->
                val price = room.price
                price == null || price.compareTo(BigDecimal("7000")) <= 0
            }
        val room2FromPriceList = room2ListPriceBody.first { it.roomId == room2Id }
        assertThat(room2FromPriceList).hasSameValuesAs(expectedRoom2)

        val room2GetResp = branch2Api.getRoom(room2Id)
        assertThat(room2GetResp.statusCode).isEqualTo(HttpStatus.OK)
        val room2GetBody = requireNotNull(room2GetResp.body)
        assertThat(room2GetBody).hasSameValuesAs(expectedRoom2)

        val room2UpdateResp = branch2Api.updateRoom(
            room2Id,
            BranchRoomRequest(number = "B2-$guest2Id", type = "suite", price = BigDecimal("9000"))
        )
        assertThat(room2UpdateResp.statusCode).isEqualTo(HttpStatus.OK)
        val expectedRoom2Updated = BranchRoomDto(roomId = room2Id, number = "B2-$guest2Id", type = "suite", price = BigDecimal("9000"))
        val room2UpdateBody = requireNotNull(room2UpdateResp.body)
        assertThat(room2UpdateBody).hasSameValuesAs(expectedRoom2Updated)
        val room2UpdatedRecordCandidate = branchRoomRepository.findById(branch2Code, room2Id)
        val room2UpdatedRecord = assertThat(room2UpdatedRecordCandidate).isNotNullValue()
        val expectedRoom2UpdatedRecord = RoomRecord(room2Id, "B2-$guest2Id", "suite", BigDecimal("9000"))
        assertThat(room2UpdatedRecord).hasSameValuesAs(expectedRoom2UpdatedRecord)

        val room2ListTypeUpdatedResp = branch2Api.listRooms(type = "suite")
        assertThat(room2ListTypeUpdatedResp.statusCode).isEqualTo(HttpStatus.OK)
        val room2ListTypeUpdatedBody = requireNotNull(room2ListTypeUpdatedResp.body)
        assertThat(room2ListTypeUpdatedBody)
            .isNotEmpty
            .anyMatch { it.roomId == room2Id }
        val room2UpdatedFromTypeList = room2ListTypeUpdatedBody.first { it.roomId == room2Id }
        assertThat(room2UpdatedFromTypeList).hasSameValuesAs(expectedRoom2Updated)

        val room2UpdateMissingResp = branch2Api.updateRoom(
            room2Id + 99999,
            BranchRoomRequest(number = "B2-missing", type = "single", price = BigDecimal("1000")),
            ApiErrorResponseBody::class.java
        )
        assertThat(room2UpdateMissingResp).correspondsToNotFoundResponse("Номер с id = ${room2Id + 99999} не найден")

        // -------- Branch (branch2): reservation --------
        val checkIn2 = LocalDate.now().plusDays(3)
        val checkOut2 = checkIn2.plusDays(2)
        val reservation2Resp = branch2Api.createReservation(
            BranchReservationRequest(guest2Id, room2Id, checkIn2, checkOut2)
        )
        assertThat(reservation2Resp.statusCode).isEqualTo(HttpStatus.CREATED)
        val reservation2Body = requireNotNull(reservation2Resp.body)
        val reservation2Id = reservation2Body.reservationId
        val expectedReservation2 = BranchReservationDto(reservationId = reservation2Id, guestId = guest2Id, roomId = room2Id, checkIn = checkIn2, checkOut = checkOut2)
        assertThat(reservation2Body).hasSameValuesAs(expectedReservation2)
        val reservation2RecordCandidate = branchReservationRepository.findById(branch2Code, reservation2Id)
        val reservation2Record = assertThat(reservation2RecordCandidate).isNotNullValue()
        val expectedReservation2Record = ReservationRecord(reservation2Id, guest2Id, room2Id, checkIn2, checkOut2)
        assertThat(reservation2Record).hasSameValuesAs(expectedReservation2Record)

        val reservation2GetResp = branch2Api.getReservation(reservation2Id)
        assertThat(reservation2GetResp.statusCode).isEqualTo(HttpStatus.OK)
        val reservation2GetBody = requireNotNull(reservation2GetResp.body)
        assertThat(reservation2GetBody).hasSameValuesAs(expectedReservation2)

        val reservation2ListResp = branch2Api.listReservations()
        assertThat(reservation2ListResp.statusCode).isEqualTo(HttpStatus.OK)
        val reservation2ListBody = requireNotNull(reservation2ListResp.body)
        assertThat(reservation2ListBody)
            .isNotEmpty
            .anyMatch { it.reservationId == reservation2Id }
        val reservation2FromListAll = reservation2ListBody.first { it.reservationId == reservation2Id }
        assertThat(reservation2FromListAll).hasSameValuesAs(expectedReservation2)

        val reservation2ListByGuestResp = branch2Api.listReservations(guestId = guest2Id)
        assertThat(reservation2ListByGuestResp.statusCode).isEqualTo(HttpStatus.OK)
        val reservation2ListByGuestBody = requireNotNull(reservation2ListByGuestResp.body)
        assertThat(reservation2ListByGuestBody)
            .isNotEmpty
            .allMatch { it.guestId == guest2Id }
        val reservation2FromGuestList = reservation2ListByGuestBody.first { it.reservationId == reservation2Id }
        assertThat(reservation2FromGuestList).hasSameValuesAs(expectedReservation2)

        val reservation2ListByRoomResp = branch2Api.listReservations(roomId = room2Id)
        assertThat(reservation2ListByRoomResp.statusCode).isEqualTo(HttpStatus.OK)
        val reservation2ListByRoomBody = requireNotNull(reservation2ListByRoomResp.body)
        assertThat(reservation2ListByRoomBody)
            .isNotEmpty
            .allMatch { it.roomId == room2Id }
        val reservation2FromRoomList = reservation2ListByRoomBody.first { it.reservationId == reservation2Id }
        assertThat(reservation2FromRoomList).hasSameValuesAs(expectedReservation2)

        val updatedCheckIn2 = checkIn2.plusDays(1)
        val updatedCheckOut2 = checkOut2.plusDays(1)
        val reservation2UpdateResp = branch2Api.updateReservation(
            reservation2Id,
            BranchReservationRequest(guest2Id, room2Id, updatedCheckIn2, updatedCheckOut2)
        )
        assertThat(reservation2UpdateResp.statusCode).isEqualTo(HttpStatus.OK)
        val expectedReservation2Updated = BranchReservationDto(reservationId = reservation2Id, guestId = guest2Id, roomId = room2Id, checkIn = updatedCheckIn2, checkOut = updatedCheckOut2)
        val reservation2UpdateBody = requireNotNull(reservation2UpdateResp.body)
        assertThat(reservation2UpdateBody).hasSameValuesAs(expectedReservation2Updated)
        val reservation2UpdatedRecordCandidate = branchReservationRepository.findById(branch2Code, reservation2Id)
        val reservation2UpdatedRecord = assertThat(reservation2UpdatedRecordCandidate).isNotNullValue()
        val expectedReservation2UpdatedRecord = ReservationRecord(reservation2Id, guest2Id, room2Id, updatedCheckIn2, updatedCheckOut2)
        assertThat(reservation2UpdatedRecord).hasSameValuesAs(expectedReservation2UpdatedRecord)

        val reservation2GetUpdatedResp = branch2Api.getReservation(reservation2Id)
        assertThat(reservation2GetUpdatedResp.statusCode).isEqualTo(HttpStatus.OK)
        val reservation2GetUpdatedBody = requireNotNull(reservation2GetUpdatedResp.body)
        assertThat(reservation2GetUpdatedBody).hasSameValuesAs(expectedReservation2Updated)

        val reservation2ListByDateResp = branch2Api.listReservations(from = updatedCheckIn2, to = updatedCheckIn2)
        assertThat(reservation2ListByDateResp.statusCode).isEqualTo(HttpStatus.OK)
        val reservation2ListByDateBody = requireNotNull(reservation2ListByDateResp.body)
        assertThat(reservation2ListByDateBody)
            .isNotEmpty
            .allMatch { reservation ->
                !reservation.checkIn.isBefore(updatedCheckIn2) && !reservation.checkIn.isAfter(updatedCheckIn2)
            }
        val reservation2FromDateList = reservation2ListByDateBody.first { it.reservationId == reservation2Id }
        assertThat(reservation2FromDateList).hasSameValuesAs(expectedReservation2Updated)

        val reservation2UpdateMissingResp = branch2Api.updateReservation(
            reservation2Id + 99999,
            BranchReservationRequest(guest2Id, room2Id, updatedCheckIn2, updatedCheckOut2),
            ApiErrorResponseBody::class.java
        )
        assertThat(reservation2UpdateMissingResp).correspondsToNotFoundResponse("Бронирование с id = ${reservation2Id + 99999} не найдено")

        // -------- Branch (branch2): staff + reservation_staff --------
        val staff2Resp = branch2Api.createStaff(BranchStaffRequest(name = "Staff $suffix2", role = "receptionist"))
        assertThat(staff2Resp.statusCode).isEqualTo(HttpStatus.CREATED)
        val staff2Body = requireNotNull(staff2Resp.body)
        val staff2Id = staff2Body.staffId
        val expectedStaff2 = BranchStaffDto(staffId = staff2Id, name = "Staff $suffix2", role = "receptionist")
        assertThat(staff2Body).hasSameValuesAs(expectedStaff2)
        val staff2RecordCandidate = branchStaffRepository.findById(branch2Code, staff2Id)
        val staff2Record = assertThat(staff2RecordCandidate).isNotNullValue()
        val expectedStaff2Record = StaffRecord(staff2Id, "Staff $suffix2", "receptionist")
        assertThat(staff2Record).hasSameValuesAs(expectedStaff2Record)

        val staff2ListResp = branch2Api.listStaff()
        assertThat(staff2ListResp.statusCode).isEqualTo(HttpStatus.OK)
        val staff2ListBody = requireNotNull(staff2ListResp.body)
        assertThat(staff2ListBody)
            .isNotEmpty
            .anyMatch { it.staffId == staff2Id }
        val staff2FromList = staff2ListBody.first { it.staffId == staff2Id }
        assertThat(staff2FromList).hasSameValuesAs(expectedStaff2)

        val staff2GetResp = branch2Api.getStaff(staff2Id)
        assertThat(staff2GetResp.statusCode).isEqualTo(HttpStatus.OK)
        val staff2GetBody = requireNotNull(staff2GetResp.body)
        assertThat(staff2GetBody).hasSameValuesAs(expectedStaff2)

        val staff2UpdateResp = branch2Api.updateStaff(
            staff2Id,
            BranchStaffRequest(name = "Staff $suffix2 Updated", role = "manager")
        )
        assertThat(staff2UpdateResp.statusCode).isEqualTo(HttpStatus.OK)
        val expectedStaff2Updated = BranchStaffDto(staffId = staff2Id, name = "Staff $suffix2 Updated", role = "manager")
        val staff2UpdateBody = requireNotNull(staff2UpdateResp.body)
        assertThat(staff2UpdateBody).hasSameValuesAs(expectedStaff2Updated)
        val staff2UpdatedRecordCandidate = branchStaffRepository.findById(branch2Code, staff2Id)
        val staff2UpdatedRecord = assertThat(staff2UpdatedRecordCandidate).isNotNullValue()
        val expectedStaff2UpdatedRecord = StaffRecord(staff2Id, "Staff $suffix2 Updated", "manager")
        assertThat(staff2UpdatedRecord).hasSameValuesAs(expectedStaff2UpdatedRecord)

        val staff2ListAfterUpdateResp = branch2Api.listStaff()
        assertThat(staff2ListAfterUpdateResp.statusCode).isEqualTo(HttpStatus.OK)
        val staff2ListAfterUpdateBody = requireNotNull(staff2ListAfterUpdateResp.body)
        assertThat(staff2ListAfterUpdateBody).isNotEmpty
        val staff2AfterUpdateFromList = staff2ListAfterUpdateBody.first { it.staffId == staff2Id }
        assertThat(staff2AfterUpdateFromList).hasSameValuesAs(expectedStaff2Updated)

        val staff2UpdateMissingResp = branch2Api.updateStaff(
            staff2Id + 99999,
            BranchStaffRequest(name = "Staff Missing", role = "manager"),
            ApiErrorResponseBody::class.java
        )
        assertThat(staff2UpdateMissingResp).correspondsToNotFoundResponse("Сотрудник с id = ${staff2Id + 99999} не найден")

        val expectedReservationStaff2 = BranchReservationStaffDto(reservationId = reservation2Id, staffId = staff2Id)
        val linkStaff2Resp = branch2Api.linkStaff(reservation2Id, staff2Id)
        assertThat(linkStaff2Resp.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(linkStaff2Resp.body).isNull()
        val reservationStaff2RecordCandidate = branchReservationStaffRepository.findById(branch2Code, reservation2Id, staff2Id)
        val reservationStaff2Record = assertThat(reservationStaff2RecordCandidate).isNotNullValue()
        val expectedReservationStaff2Record = ReservationStaffRecord(reservation2Id, staff2Id)
        assertThat(reservationStaff2Record).hasSameValuesAs(expectedReservationStaff2Record)

        val reservationStaff2Resp = branch2Api.listReservationStaff(reservation2Id)
        assertThat(reservationStaff2Resp.statusCode).isEqualTo(HttpStatus.OK)
        val reservationStaff2Body = requireNotNull(reservationStaff2Resp.body)
        assertThat(reservationStaff2Body)
            .isNotEmpty
            .anyMatch { it.staffId == staff2Id }
        val reservationStaff2FromList = reservationStaff2Body.first { it.staffId == staff2Id }
        assertThat(reservationStaff2FromList).hasSameValuesAs(expectedReservationStaff2)

        val branch1CountsAfterBranch2 = captureBranchCounts(branchCode)
        assertThat(branch1CountsAfterBranch2).isEqualTo(branch1CountsBeforeBranch2)

        cleanupBranch(
            branchApi = branch2Api,
            branchCode = "branch2",
            guestId = guest2Id,
            roomId = room2Id,
            reservationId = reservation2Id,
            staffId = staff2Id
        )
    }

    private fun cleanupCentral(
        supplierId: Int,
        branchId: Int,
        staffId: Int,
        departmentId: Int
    ) {
        val unlinkResp = centralApi.unlinkSupplierBranch(supplierId, branchId)
        assertThat(unlinkResp.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
        val actualSupplierBranchAfterUnlink = centralSupplierBranchRepository.findById(supplierId, branchId)
        assertThat(actualSupplierBranchAfterUnlink).isNull()

        val linkedBranchesAfterUnlinkResp = centralApi.listSupplierBranches(supplierId)
        assertThat(linkedBranchesAfterUnlinkResp.statusCode).isEqualTo(HttpStatus.OK)
        val linkedBranchesAfterUnlinkBody = requireNotNull(linkedBranchesAfterUnlinkResp.body)
        assertThat(linkedBranchesAfterUnlinkBody).noneMatch { it.branchId == branchId }

        val unlinkMissingResp = centralApi.unlinkSupplierBranch(supplierId, branchId, ApiErrorResponseBody::class.java)
        assertThat(unlinkMissingResp).correspondsToNotFoundResponse("Связь поставщик=$supplierId филиал=$branchId не найдена")

        val supplierDeleteResp = centralApi.deleteSupplier(supplierId)
        assertThat(supplierDeleteResp.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
        val actualSupplierAfterDeleteRecord = centralSupplierRepository.findById(supplierId)
        assertThat(actualSupplierAfterDeleteRecord).isNull()

        val supplierListAfterDeleteResp = centralApi.listSuppliers()
        assertThat(supplierListAfterDeleteResp.statusCode).isEqualTo(HttpStatus.OK)
        val supplierListAfterDeleteBody = requireNotNull(supplierListAfterDeleteResp.body)
        assertThat(supplierListAfterDeleteBody).noneMatch { it.supplierId == supplierId }

        val supplierGetMissingResp = centralApi.getSupplier(supplierId, ApiErrorResponseBody::class.java)
        assertThat(supplierGetMissingResp).correspondsToNotFoundResponse("Поставщик с id = $supplierId не найден")

        val supplierDeleteMissingResp = centralApi.deleteSupplier(supplierId, ApiErrorResponseBody::class.java)
        assertThat(supplierDeleteMissingResp).correspondsToNotFoundResponse("Поставщик с id = $supplierId не найден")

        val staffDeleteResp = centralApi.deleteStaff(staffId)
        assertThat(staffDeleteResp.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
        val actualStaffAfterDeleteRecord = centralStaffRepository.findById(staffId)
        assertThat(actualStaffAfterDeleteRecord).isNull()

        val staffListAfterDeleteResp = centralApi.listStaff(departmentId)
        assertThat(staffListAfterDeleteResp.statusCode).isEqualTo(HttpStatus.OK)
        val staffListAfterDeleteBody = requireNotNull(staffListAfterDeleteResp.body)
        assertThat(staffListAfterDeleteBody).noneMatch { it.staffId == staffId }

        val staffGetMissingResp = centralApi.getStaff(staffId, ApiErrorResponseBody::class.java)
        assertThat(staffGetMissingResp).correspondsToNotFoundResponse("Сотрудник центрального узла с id = $staffId не найден")

        val staffDeleteMissingResp = centralApi.deleteStaff(staffId, ApiErrorResponseBody::class.java)
        assertThat(staffDeleteMissingResp).correspondsToNotFoundResponse("Сотрудник центрального узла с id = $staffId не найден")

        val departmentDeleteResp = centralApi.deleteDepartment(departmentId)
        assertThat(departmentDeleteResp.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
        val actualDepartmentAfterDeleteRecord = centralDepartmentRepository.findById(departmentId)
        assertThat(actualDepartmentAfterDeleteRecord).isNull()

        val departmentListAfterDeleteResp = centralApi.listDepartments()
        assertThat(departmentListAfterDeleteResp.statusCode).isEqualTo(HttpStatus.OK)
        val departmentListAfterDeleteBody = requireNotNull(departmentListAfterDeleteResp.body)
        assertThat(departmentListAfterDeleteBody).noneMatch { it.departmentId == departmentId }

        val departmentGetMissingResp = centralApi.getDepartment(departmentId, ApiErrorResponseBody::class.java)
        assertThat(departmentGetMissingResp).correspondsToNotFoundResponse("Департамент с id = $departmentId не найден")

        val departmentDeleteMissingResp = centralApi.deleteDepartment(departmentId, ApiErrorResponseBody::class.java)
        assertThat(departmentDeleteMissingResp).correspondsToNotFoundResponse("Департамент с id = $departmentId не найден")
    }

    private fun cleanupBranch(
        branchApi: BranchApiClient,
        branchCode: String,
        guestId: Int,
        roomId: Int,
        reservationId: Int,
        staffId: Int
    ) {

        val unlinkStaffResp = branchApi.unlinkStaff(reservationId, staffId)
        assertThat(unlinkStaffResp.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
        val actualReservationStaffAfterUnlinkRecord = branchReservationStaffRepository.findById(branchCode, reservationId, staffId)
        assertThat(actualReservationStaffAfterUnlinkRecord).isNull()

        val reservationStaffAfterUnlinkResp = branchApi.listReservationStaff(reservationId)
        assertThat(reservationStaffAfterUnlinkResp.statusCode).isEqualTo(HttpStatus.OK)
        val reservationStaffAfterUnlinkBody = requireNotNull(reservationStaffAfterUnlinkResp.body)
        assertThat(reservationStaffAfterUnlinkBody).noneMatch { it.staffId == staffId }

        val unlinkStaffMissingResp = branchApi.unlinkStaff(reservationId, staffId, ApiErrorResponseBody::class.java)
        assertThat(unlinkStaffMissingResp).correspondsToNotFoundResponse("Связь бронирование=$reservationId сотрудник=$staffId не найдена")

        val reservationDeleteResp = branchApi.deleteReservation(reservationId)
        assertThat(reservationDeleteResp.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
        val actualReservationAfterDeleteRecord = branchReservationRepository.findById(branchCode, reservationId)
        assertThat(actualReservationAfterDeleteRecord).isNull()

        val reservationListAfterDeleteResp = branchApi.listReservations()
        assertThat(reservationListAfterDeleteResp.statusCode).isEqualTo(HttpStatus.OK)
        val reservationListAfterDeleteBody = requireNotNull(reservationListAfterDeleteResp.body)
        assertThat(reservationListAfterDeleteBody).noneMatch { it.reservationId == reservationId }

        val reservationGetMissingResp = branchApi.getReservation(reservationId, ApiErrorResponseBody::class.java)
        assertThat(reservationGetMissingResp).correspondsToNotFoundResponse("Бронирование с id = $reservationId не найдено")

        val reservationDeleteMissingResp = branchApi.deleteReservation(reservationId, ApiErrorResponseBody::class.java)
        assertThat(reservationDeleteMissingResp).correspondsToNotFoundResponse("Бронирование с id = $reservationId не найдено")

        val staffDeleteResp = branchApi.deleteStaff(staffId)
        assertThat(staffDeleteResp.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
        val actualBranchStaffAfterDeleteRecord = branchStaffRepository.findById(branchCode, staffId)
        assertThat(actualBranchStaffAfterDeleteRecord).isNull()

        val staffListAfterDeleteResp = branchApi.listStaff()
        assertThat(staffListAfterDeleteResp.statusCode).isEqualTo(HttpStatus.OK)
        val staffListAfterDeleteBody = requireNotNull(staffListAfterDeleteResp.body)
        assertThat(staffListAfterDeleteBody).noneMatch { it.staffId == staffId }

        val staffGetMissingResp = branchApi.getStaff(staffId, ApiErrorResponseBody::class.java)
        assertThat(staffGetMissingResp).correspondsToNotFoundResponse("Сотрудник с id = $staffId не найден")

        val staffDeleteMissingResp = branchApi.deleteStaff(staffId, ApiErrorResponseBody::class.java)
        assertThat(staffDeleteMissingResp).correspondsToNotFoundResponse("Сотрудник с id = $staffId не найден")

        val roomDeleteResp = branchApi.deleteRoom(roomId)
        assertThat(roomDeleteResp.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
        val actualRoomAfterDeleteRecord = branchRoomRepository.findById(branchCode, roomId)
        assertThat(actualRoomAfterDeleteRecord).isNull()

        val roomListAfterDeleteResp = branchApi.listRooms()
        assertThat(roomListAfterDeleteResp.statusCode).isEqualTo(HttpStatus.OK)
        val roomListAfterDeleteBody = requireNotNull(roomListAfterDeleteResp.body)
        assertThat(roomListAfterDeleteBody).noneMatch { it.roomId == roomId }

        val roomGetMissingResp = branchApi.getRoom(roomId, ApiErrorResponseBody::class.java)
        assertThat(roomGetMissingResp).correspondsToNotFoundResponse("Номер с id = $roomId не найден")

        val roomDeleteMissingResp = branchApi.deleteRoom(roomId, ApiErrorResponseBody::class.java)
        assertThat(roomDeleteMissingResp).correspondsToNotFoundResponse("Номер с id = $roomId не найден")

        val guestDeleteResp = branchApi.deleteGuest(guestId)
        assertThat(guestDeleteResp.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
        val actualGuestAfterDeleteRecord = branchGuestRepository.findById(branchCode, guestId)
        assertThat(actualGuestAfterDeleteRecord).isNull()

        val guestListAfterDeleteResp = branchApi.listGuests()
        assertThat(guestListAfterDeleteResp.statusCode).isEqualTo(HttpStatus.OK)
        val guestListAfterDeleteBody = requireNotNull(guestListAfterDeleteResp.body)
        assertThat(guestListAfterDeleteBody).noneMatch { it.guestId == guestId }

        val guestGetMissingResp = branchApi.getGuest(guestId, ApiErrorResponseBody::class.java)
        assertThat(guestGetMissingResp).correspondsToNotFoundResponse("Гость с id = $guestId не найден")

        val guestDeleteMissingResp = branchApi.deleteGuest(guestId, ApiErrorResponseBody::class.java)
        assertThat(guestDeleteMissingResp).correspondsToNotFoundResponse("Гость с id = $guestId не найден")
    }


    private data class BranchCounts(
        val guests: Int,
        val rooms: Int,
        val reservations: Int,
        val staff: Int,
        val reservationStaff: Int
    )

    private fun captureBranchCounts(branchCode: String): BranchCounts =
        BranchCounts(
            guests = branchGuestRepository.findAll(branchCode).size,
            rooms = branchRoomRepository.findAll(branchCode, null, null).size,
            reservations = branchReservationRepository.findAll(branchCode, null, null, null, null).size,
            staff = branchStaffRepository.findAll(branchCode).size,
            reservationStaff = branchReservationStaffRepository.findAll(branchCode).size
        )

}
