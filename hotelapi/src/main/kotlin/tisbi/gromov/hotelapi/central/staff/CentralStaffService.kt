package tisbi.gromov.hotelapi.central.staff

interface CentralStaffService {
    fun list(departmentId: Int?): List<CentralStaffDto>
    fun get(staffId: Int): CentralStaffDto
    fun create(req: CentralStaffCreateRequest): CentralStaffDto
    fun update(staffId: Int, req: CentralStaffUpdateRequest): CentralStaffDto
    fun delete(staffId: Int)
}
