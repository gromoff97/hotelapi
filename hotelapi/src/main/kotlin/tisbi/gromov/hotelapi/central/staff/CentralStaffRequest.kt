package tisbi.gromov.hotelapi.central.staff

data class CentralStaffCreateRequest(
    val name: String,
    val departmentId: Int
)

data class CentralStaffUpdateRequest(
    val name: String,
    val departmentId: Int
)
