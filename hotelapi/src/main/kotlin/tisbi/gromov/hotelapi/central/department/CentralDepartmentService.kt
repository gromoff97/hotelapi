package tisbi.gromov.hotelapi.central.department

interface CentralDepartmentService {
    fun list(name: String?): List<CentralDepartmentDto>
    fun get(departmentId: Int): CentralDepartmentDto
    fun create(req: CentralDepartmentCreateRequest): CentralDepartmentDto
    fun update(departmentId: Int, req: CentralDepartmentUpdateRequest): CentralDepartmentDto
    fun delete(departmentId: Int)
}
