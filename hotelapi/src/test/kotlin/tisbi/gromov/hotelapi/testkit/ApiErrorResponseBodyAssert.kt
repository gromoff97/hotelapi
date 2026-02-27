package tisbi.gromov.hotelapi.testkit

import org.assertj.core.api.AbstractAssert
import org.assertj.core.api.Assertions
import org.springframework.http.HttpStatus
import tisbi.gromov.hotelapi.utils.ApiErrorResponseBody

class ApiErrorResponseBodyAssert(actual: ApiErrorResponseBody?) :
    AbstractAssert<ApiErrorResponseBodyAssert, ApiErrorResponseBody?>(actual, ApiErrorResponseBodyAssert::class.java) {

    fun isNotFoundWithMessage(message: String): ApiErrorResponseBodyAssert =
        apply {
            isNotNull()
            val actualValue = actual!!
            Assertions.assertThat(actualValue.status).isEqualTo(HttpStatus.NOT_FOUND.value())
            Assertions.assertThat(actualValue.error).isEqualTo("Не найдено")
            Assertions.assertThat(actualValue.message).isEqualTo(message)
            Assertions.assertThat(actualValue.timestamp).isNotNull
        }

    fun isConflictWithMessage(message: String): ApiErrorResponseBodyAssert =
        apply {
            isNotNull()
            val actualValue = actual!!
            Assertions.assertThat(actualValue.status).isEqualTo(HttpStatus.CONFLICT.value())
            Assertions.assertThat(actualValue.error).isEqualTo("Конфликт данных")
            Assertions.assertThat(actualValue.message).isEqualTo(message)
            Assertions.assertThat(actualValue.timestamp).isNotNull
        }
}
