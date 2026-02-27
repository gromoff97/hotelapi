package tisbi.gromov.hotelapi.testkit

import org.assertj.core.api.AbstractAssert
import org.assertj.core.api.Assertions
import org.jooq.Record
import java.math.BigDecimal

class RecordAssert(actual: Record?) : AbstractAssert<RecordAssert, Record?>(actual, RecordAssert::class.java) {

    fun isNotNullValue(): Record {
        isNotNull()
        return actual!!
    }

    fun hasSameValuesAs(expected: Record): RecordAssert =
        apply {
            isNotNull()
            val actualRecord = actual!!
            expected.fields().forEach { field ->
                val expectedValue = expected.get(field)
                val actualValue = actualRecord.get(field)
                if (expectedValue is BigDecimal) {
                    Assertions.assertThat(actualValue).isNotNull
                    val actualBigDecimal = actualValue as BigDecimal
                    Assertions.assertThat(actualBigDecimal.compareTo(expectedValue)).isZero()
                } else {
                    Assertions.assertThat(actualValue).isEqualTo(expectedValue)
                }
            }
        }
}
