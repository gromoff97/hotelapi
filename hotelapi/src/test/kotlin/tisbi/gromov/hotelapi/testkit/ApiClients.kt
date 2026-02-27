package tisbi.gromov.hotelapi.testkit

import org.springframework.http.ResponseEntity
import tisbi.gromov.hotelapi.branch.guest.BranchGuestDto
import tisbi.gromov.hotelapi.branch.guest.BranchGuestRequest
import tisbi.gromov.hotelapi.branch.reservation.BranchReservationDto
import tisbi.gromov.hotelapi.branch.reservation.BranchReservationRequest
import tisbi.gromov.hotelapi.branch.reservation_staff.BranchReservationStaffDto
import tisbi.gromov.hotelapi.branch.room.BranchRoomDto
import tisbi.gromov.hotelapi.branch.room.BranchRoomRequest
import tisbi.gromov.hotelapi.branch.staff.BranchStaffDto
import tisbi.gromov.hotelapi.branch.staff.BranchStaffRequest
import tisbi.gromov.hotelapi.central.branch.CentralBranchDto
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
import java.math.BigDecimal
import java.time.LocalDate

class CentralRestApiClient(private val client: ApiClient) {
    fun listDepartments(name: String? = null): ResponseEntity<Array<CentralDepartmentDto>> =
        client.get(centralDepartmentsPath(name), Array<CentralDepartmentDto>::class.java)

    fun <T> getDepartment(departmentId: Int, responseType: Class<T>): ResponseEntity<T> =
        client.get(centralDepartmentPath(departmentId), responseType)

    fun getDepartment(departmentId: Int): ResponseEntity<CentralDepartmentDto> =
        getDepartment(departmentId, CentralDepartmentDto::class.java)

    fun <T> createDepartment(request: CentralDepartmentCreateRequest, responseType: Class<T>): ResponseEntity<T> =
        client.post(centralDepartmentsPath(), request, responseType)

    fun createDepartment(request: CentralDepartmentCreateRequest): ResponseEntity<CentralDepartmentDto> =
        createDepartment(request, CentralDepartmentDto::class.java)

    fun <T> updateDepartment(
        departmentId: Int,
        request: CentralDepartmentUpdateRequest,
        responseType: Class<T>
    ): ResponseEntity<T> =
        client.put(centralDepartmentPath(departmentId), request, responseType)

    fun updateDepartment(departmentId: Int, request: CentralDepartmentUpdateRequest): ResponseEntity<CentralDepartmentDto> =
        updateDepartment(departmentId, request, CentralDepartmentDto::class.java)

    fun <T> deleteDepartment(departmentId: Int, responseType: Class<T>): ResponseEntity<T> =
        client.delete(centralDepartmentPath(departmentId), responseType)

    fun deleteDepartment(departmentId: Int): ResponseEntity<Void> =
        client.delete(centralDepartmentPath(departmentId))

    fun listStaff(departmentId: Int? = null): ResponseEntity<Array<CentralStaffDto>> =
        client.get(centralStaffPath(departmentId), Array<CentralStaffDto>::class.java)

    fun <T> getStaff(staffId: Int, responseType: Class<T>): ResponseEntity<T> =
        client.get(centralStaffPath(staffId), responseType)

    fun getStaff(staffId: Int): ResponseEntity<CentralStaffDto> =
        getStaff(staffId, CentralStaffDto::class.java)

    fun <T> createStaff(request: CentralStaffCreateRequest, responseType: Class<T>): ResponseEntity<T> =
        client.post(centralStaffPath(), request, responseType)

    fun createStaff(request: CentralStaffCreateRequest): ResponseEntity<CentralStaffDto> =
        createStaff(request, CentralStaffDto::class.java)

    fun <T> updateStaff(staffId: Int, request: CentralStaffUpdateRequest, responseType: Class<T>): ResponseEntity<T> =
        client.put(centralStaffPath(staffId), request, responseType)

    fun updateStaff(staffId: Int, request: CentralStaffUpdateRequest): ResponseEntity<CentralStaffDto> =
        updateStaff(staffId, request, CentralStaffDto::class.java)

    fun <T> deleteStaff(staffId: Int, responseType: Class<T>): ResponseEntity<T> =
        client.delete(centralStaffPath(staffId), responseType)

    fun deleteStaff(staffId: Int): ResponseEntity<Void> =
        client.delete(centralStaffPath(staffId))

    fun listSuppliers(companyName: String? = null): ResponseEntity<Array<CentralSupplierDto>> =
        client.get(centralSuppliersPath(companyName), Array<CentralSupplierDto>::class.java)

    fun <T> getSupplier(supplierId: Int, responseType: Class<T>): ResponseEntity<T> =
        client.get(centralSupplierPath(supplierId), responseType)

    fun getSupplier(supplierId: Int): ResponseEntity<CentralSupplierDto> =
        getSupplier(supplierId, CentralSupplierDto::class.java)

    fun <T> createSupplier(request: CentralSupplierCreateRequest, responseType: Class<T>): ResponseEntity<T> =
        client.post(centralSuppliersPath(), request, responseType)

    fun createSupplier(request: CentralSupplierCreateRequest): ResponseEntity<CentralSupplierDto> =
        createSupplier(request, CentralSupplierDto::class.java)

    fun <T> updateSupplier(
        supplierId: Int,
        request: CentralSupplierUpdateRequest,
        responseType: Class<T>
    ): ResponseEntity<T> =
        client.put(centralSupplierPath(supplierId), request, responseType)

    fun updateSupplier(supplierId: Int, request: CentralSupplierUpdateRequest): ResponseEntity<CentralSupplierDto> =
        updateSupplier(supplierId, request, CentralSupplierDto::class.java)

    fun <T> deleteSupplier(supplierId: Int, responseType: Class<T>): ResponseEntity<T> =
        client.delete(centralSupplierPath(supplierId), responseType)

    fun deleteSupplier(supplierId: Int): ResponseEntity<Void> =
        client.delete(centralSupplierPath(supplierId))

    fun listBranches(): ResponseEntity<Array<CentralBranchDto>> =
        client.get(centralBranchesPath(), Array<CentralBranchDto>::class.java)

    fun <T> getBranch(branchId: Int, responseType: Class<T>): ResponseEntity<T> =
        client.get(centralBranchPath(branchId), responseType)

    fun getBranch(branchId: Int): ResponseEntity<CentralBranchDto> =
        getBranch(branchId, CentralBranchDto::class.java)

    fun <T> linkSupplierBranch(supplierId: Int, branchId: Int, responseType: Class<T>): ResponseEntity<T> =
        client.post(centralSupplierBranchPath(supplierId, branchId), null, responseType)

    fun linkSupplierBranch(supplierId: Int, branchId: Int): ResponseEntity<CentralSupplierBranchDto> =
        linkSupplierBranch(supplierId, branchId, CentralSupplierBranchDto::class.java)

    fun listSupplierBranches(supplierId: Int): ResponseEntity<Array<CentralSupplierBranchDto>> =
        client.get(centralSupplierBranchesPath(supplierId), Array<CentralSupplierBranchDto>::class.java)

    fun <T> unlinkSupplierBranch(supplierId: Int, branchId: Int, responseType: Class<T>): ResponseEntity<T> =
        client.delete(centralSupplierBranchPath(supplierId, branchId), responseType)

    fun unlinkSupplierBranch(supplierId: Int, branchId: Int): ResponseEntity<Void> =
        client.delete(centralSupplierBranchPath(supplierId, branchId))

    private fun centralDepartmentsPath(name: String? = null): String =
        if (name.isNullOrBlank()) "/api/v1/central/departments" else "/api/v1/central/departments?name=$name"

    private fun centralDepartmentPath(departmentId: Int): String =
        "/api/v1/central/departments/$departmentId"

    private fun centralStaffPath(): String = "/api/v1/central/staff"

    private fun centralStaffPath(departmentId: Int?): String =
        if (departmentId == null) "/api/v1/central/staff" else "/api/v1/central/staff?departmentId=$departmentId"

    private fun centralStaffPath(staffId: Int): String = "/api/v1/central/staff/$staffId"

    private fun centralSuppliersPath(companyName: String? = null): String =
        if (companyName.isNullOrBlank()) "/api/v1/central/suppliers" else "/api/v1/central/suppliers?companyName=$companyName"

    private fun centralSupplierPath(supplierId: Int): String = "/api/v1/central/suppliers/$supplierId"

    private fun centralBranchesPath(): String = "/api/v1/central/branches"

    private fun centralBranchPath(branchId: Int): String = "/api/v1/central/branches/$branchId"

    private fun centralSupplierBranchesPath(supplierId: Int): String =
        "/api/v1/central/suppliers/$supplierId/branches"

    private fun centralSupplierBranchPath(supplierId: Int, branchId: Int): String =
        "/api/v1/central/suppliers/$supplierId/branches/$branchId"
}

class BranchApiClient(
    private val client: ApiClient,
    private val branchCode: String
) {
    fun listGuests(): ResponseEntity<Array<BranchGuestDto>> =
        client.get(branchGuestsPath(), Array<BranchGuestDto>::class.java)

    fun <T> createGuest(request: BranchGuestRequest, responseType: Class<T>): ResponseEntity<T> =
        client.post(branchGuestsPath(), request, responseType)

    fun createGuest(request: BranchGuestRequest): ResponseEntity<BranchGuestDto> =
        createGuest(request, BranchGuestDto::class.java)

    fun <T> getGuest(guestId: Int, responseType: Class<T>): ResponseEntity<T> =
        client.get(branchGuestPath(guestId), responseType)

    fun getGuest(guestId: Int): ResponseEntity<BranchGuestDto> =
        getGuest(guestId, BranchGuestDto::class.java)

    fun <T> updateGuest(guestId: Int, request: BranchGuestRequest, responseType: Class<T>): ResponseEntity<T> =
        client.put(branchGuestPath(guestId), request, responseType)

    fun updateGuest(guestId: Int, request: BranchGuestRequest): ResponseEntity<BranchGuestDto> =
        updateGuest(guestId, request, BranchGuestDto::class.java)

    fun <T> deleteGuest(guestId: Int, responseType: Class<T>): ResponseEntity<T> =
        client.delete(branchGuestPath(guestId), responseType)

    fun deleteGuest(guestId: Int): ResponseEntity<Void> =
        client.delete(branchGuestPath(guestId))

    fun listRooms(type: String? = null, maxPrice: BigDecimal? = null): ResponseEntity<Array<BranchRoomDto>> =
        client.get(branchRoomsQueryPath(type, maxPrice), Array<BranchRoomDto>::class.java)

    fun <T> createRoom(request: BranchRoomRequest, responseType: Class<T>): ResponseEntity<T> =
        client.post(branchRoomsPath(), request, responseType)

    fun createRoom(request: BranchRoomRequest): ResponseEntity<BranchRoomDto> =
        createRoom(request, BranchRoomDto::class.java)

    fun <T> getRoom(roomId: Int, responseType: Class<T>): ResponseEntity<T> =
        client.get(branchRoomPath(roomId), responseType)

    fun getRoom(roomId: Int): ResponseEntity<BranchRoomDto> =
        getRoom(roomId, BranchRoomDto::class.java)

    fun <T> updateRoom(roomId: Int, request: BranchRoomRequest, responseType: Class<T>): ResponseEntity<T> =
        client.put(branchRoomPath(roomId), request, responseType)

    fun updateRoom(roomId: Int, request: BranchRoomRequest): ResponseEntity<BranchRoomDto> =
        updateRoom(roomId, request, BranchRoomDto::class.java)

    fun <T> deleteRoom(roomId: Int, responseType: Class<T>): ResponseEntity<T> =
        client.delete(branchRoomPath(roomId), responseType)

    fun deleteRoom(roomId: Int): ResponseEntity<Void> =
        client.delete(branchRoomPath(roomId))

    fun listStaff(): ResponseEntity<Array<BranchStaffDto>> =
        client.get(branchStaffPath(), Array<BranchStaffDto>::class.java)

    fun <T> createStaff(request: BranchStaffRequest, responseType: Class<T>): ResponseEntity<T> =
        client.post(branchStaffPath(), request, responseType)

    fun createStaff(request: BranchStaffRequest): ResponseEntity<BranchStaffDto> =
        createStaff(request, BranchStaffDto::class.java)

    fun <T> getStaff(staffId: Int, responseType: Class<T>): ResponseEntity<T> =
        client.get(branchStaffPath(staffId), responseType)

    fun getStaff(staffId: Int): ResponseEntity<BranchStaffDto> =
        getStaff(staffId, BranchStaffDto::class.java)

    fun <T> updateStaff(staffId: Int, request: BranchStaffRequest, responseType: Class<T>): ResponseEntity<T> =
        client.put(branchStaffPath(staffId), request, responseType)

    fun updateStaff(staffId: Int, request: BranchStaffRequest): ResponseEntity<BranchStaffDto> =
        updateStaff(staffId, request, BranchStaffDto::class.java)

    fun <T> deleteStaff(staffId: Int, responseType: Class<T>): ResponseEntity<T> =
        client.delete(branchStaffPath(staffId), responseType)

    fun deleteStaff(staffId: Int): ResponseEntity<Void> =
        client.delete(branchStaffPath(staffId))

    fun listReservations(
        guestId: Int? = null,
        roomId: Int? = null,
        from: LocalDate? = null,
        to: LocalDate? = null
    ): ResponseEntity<Array<BranchReservationDto>> =
        client.get(branchReservationsQueryPath(guestId, roomId, from, to), Array<BranchReservationDto>::class.java)

    fun <T> createReservation(request: BranchReservationRequest, responseType: Class<T>): ResponseEntity<T> =
        client.post(branchReservationsPath(), request, responseType)

    fun createReservation(request: BranchReservationRequest): ResponseEntity<BranchReservationDto> =
        createReservation(request, BranchReservationDto::class.java)

    fun <T> getReservation(reservationId: Int, responseType: Class<T>): ResponseEntity<T> =
        client.get(branchReservationPath(reservationId), responseType)

    fun getReservation(reservationId: Int): ResponseEntity<BranchReservationDto> =
        getReservation(reservationId, BranchReservationDto::class.java)

    fun <T> updateReservation(
        reservationId: Int,
        request: BranchReservationRequest,
        responseType: Class<T>
    ): ResponseEntity<T> =
        client.put(branchReservationPath(reservationId), request, responseType)

    fun updateReservation(reservationId: Int, request: BranchReservationRequest): ResponseEntity<BranchReservationDto> =
        updateReservation(reservationId, request, BranchReservationDto::class.java)

    fun <T> deleteReservation(reservationId: Int, responseType: Class<T>): ResponseEntity<T> =
        client.delete(branchReservationPath(reservationId), responseType)

    fun deleteReservation(reservationId: Int): ResponseEntity<Void> =
        client.delete(branchReservationPath(reservationId))

    fun <T> linkStaff(reservationId: Int, staffId: Int, responseType: Class<T>): ResponseEntity<T> =
        client.post(branchReservationStaffPath(reservationId, staffId), null, responseType)

    fun linkStaff(reservationId: Int, staffId: Int): ResponseEntity<BranchReservationStaffDto> =
        linkStaff(reservationId, staffId, BranchReservationStaffDto::class.java)

    fun listReservationStaff(reservationId: Int): ResponseEntity<Array<BranchReservationStaffDto>> =
        client.get(branchReservationStaffListPath(reservationId), Array<BranchReservationStaffDto>::class.java)

    fun <T> unlinkStaff(reservationId: Int, staffId: Int, responseType: Class<T>): ResponseEntity<T> =
        client.delete(branchReservationStaffPath(reservationId, staffId), responseType)

    fun unlinkStaff(reservationId: Int, staffId: Int): ResponseEntity<Void> =
        client.delete(branchReservationStaffPath(reservationId, staffId))

    private fun branchGuestsPath(): String = "/api/v1/branches/$branchCode/guests"

    private fun branchGuestPath(guestId: Int): String = "/api/v1/branches/$branchCode/guests/$guestId"

    private fun branchRoomsPath(): String = "/api/v1/branches/$branchCode/rooms"

    private fun branchRoomsQueryPath(type: String?, maxPrice: BigDecimal?): String {
        val params = mutableListOf<String>()
        if (!type.isNullOrBlank()) {
            params.add("type=$type")
        }
        if (maxPrice != null) {
            params.add("maxPrice=$maxPrice")
        }
        return if (params.isEmpty()) branchRoomsPath() else branchRoomsPath() + "?" + params.joinToString("&")
    }

    private fun branchRoomPath(roomId: Int): String = "/api/v1/branches/$branchCode/rooms/$roomId"

    private fun branchReservationsPath(): String = "/api/v1/branches/$branchCode/reservations"

    private fun branchReservationsQueryPath(
        guestId: Int?,
        roomId: Int?,
        from: LocalDate?,
        to: LocalDate?
    ): String {
        val params = mutableListOf<String>()
        if (guestId != null) {
            params.add("guestId=$guestId")
        }
        if (roomId != null) {
            params.add("roomId=$roomId")
        }
        if (from != null) {
            params.add("from=$from")
        }
        if (to != null) {
            params.add("to=$to")
        }
        return if (params.isEmpty()) branchReservationsPath()
        else branchReservationsPath() + "?" + params.joinToString("&")
    }

    private fun branchReservationPath(reservationId: Int): String =
        "/api/v1/branches/$branchCode/reservations/$reservationId"

    private fun branchStaffPath(): String = "/api/v1/branches/$branchCode/staff"

    private fun branchStaffPath(staffId: Int): String = "/api/v1/branches/$branchCode/staff/$staffId"

    private fun branchReservationStaffListPath(reservationId: Int): String =
        "/api/v1/branches/$branchCode/reservations/$reservationId/staff"

    private fun branchReservationStaffPath(reservationId: Int, staffId: Int): String =
        "/api/v1/branches/$branchCode/reservations/$reservationId/staff/$staffId"
}
