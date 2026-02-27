package tisbi.gromov.hotelapi.central.branch

interface CentralBranchService {
    fun list(): List<CentralBranchDto>
    fun get(branchId: Int): CentralBranchDto
}