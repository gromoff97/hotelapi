package tisbi.gromov.hotelapi.testkit

import org.assertj.core.api.AbstractAssert
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import tisbi.gromov.hotelapi.utils.ApiErrorResponseBody

class ApiErrorResponseEntityAssert(actual: ResponseEntity<ApiErrorResponseBody>?) :
    AbstractAssert<ApiErrorResponseEntityAssert, ResponseEntity<ApiErrorResponseBody>?>(
        actual,
        ApiErrorResponseEntityAssert::class.java
    ) {

    fun correspondsToNotFoundResponse(message: String): ApiErrorResponseEntityAssert =
        apply {
            isNotNull()
            assertThat(actual!!.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
            CustomAssertions.assertThat(actual!!.body).isNotFoundWithMessage(message)
        }

    fun correspondsToConflictResponse(message: String): ApiErrorResponseEntityAssert =
        apply {
            isNotNull()
            assertThat(actual!!.statusCode).isEqualTo(HttpStatus.CONFLICT)
            CustomAssertions.assertThat(actual!!.body).isConflictWithMessage(message)
        }
}
