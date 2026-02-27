package tisbi.gromov.hotelapi.utils

import java.time.OffsetDateTime

data class ApiErrorResponseBody(
    val timestamp: OffsetDateTime? = null,
    val status: Int? = null,
    val error: String? = null,
    val message: String? = null,
)
