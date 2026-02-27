package tisbi.gromov.hotelapi.central.branch

data class CentralBranchCreateRequestBody(
    val name: String,
    val address: String
)

data class CentralBranchUpdateRequestBody(
    val name: String,
    val address: String
)
