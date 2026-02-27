package tisbi.gromov.hotelapi.utils

import jakarta.servlet.http.HttpServletRequest
import org.jooq.exception.DataAccessException as JooqDataAccessException
import org.springframework.dao.CannotAcquireLockException
import org.springframework.dao.DataAccessException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.DataAccessResourceFailureException
import org.springframework.dao.DuplicateKeyException
import org.springframework.dao.PessimisticLockingFailureException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.jdbc.CannotGetJdbcConnectionException
import org.springframework.jdbc.UncategorizedSQLException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.transaction.CannotCreateTransactionException
import java.sql.SQLException
import java.sql.SQLTimeoutException
import java.sql.SQLNonTransientConnectionException
import java.sql.SQLTransientConnectionException
import java.time.OffsetDateTime

@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(DatabaseRecordNotFoundException::class)
    fun handleNotFound(
        exception: DatabaseRecordNotFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ApiErrorResponseBody> = build(
        status = HttpStatus.NOT_FOUND,
        request = request,
        message = exception.message ?: "Сущность не найдена"
    )

    @ExceptionHandler(
        MethodArgumentNotValidException::class,
        MethodArgumentTypeMismatchException::class,
        MissingServletRequestParameterException::class,
        HttpMessageNotReadableException::class,
        IllegalArgumentException::class
    )
    fun handleBadRequest(exception: Exception, request: HttpServletRequest): ResponseEntity<ApiErrorResponseBody> =
        build(
            status = HttpStatus.BAD_REQUEST,
            request = request,
            message = exception.message ?: "Некорректный запрос"
        )

    @ExceptionHandler(
        DataIntegrityViolationException::class,
        DuplicateKeyException::class,
        CannotAcquireLockException::class,
        PessimisticLockingFailureException::class
    )
    fun handleConflict(exception: Exception, request: HttpServletRequest): ResponseEntity<ApiErrorResponseBody> =
        build(
            status = HttpStatus.CONFLICT,
            request = request,
            message = normalizeSqlMessage(exception.message)
        )

    @ExceptionHandler(
        CannotGetJdbcConnectionException::class,
        DataAccessResourceFailureException::class,
        SQLTimeoutException::class
    )
    fun handleServiceUnavailable(
        exception: Exception,
        request: HttpServletRequest
    ): ResponseEntity<ApiErrorResponseBody> = build(
        status = HttpStatus.SERVICE_UNAVAILABLE,
        request = request,
        message = normalizeSqlMessage(exception.message)
    )

    @ExceptionHandler(JooqDataAccessException::class)
    fun handleJooqDataAccess(
        exception: JooqDataAccessException,
        request: HttpServletRequest
    ): ResponseEntity<ApiErrorResponseBody> = when (extractSqlState(exception)) {
        "08000", "08001", "08003", "08004", "08006", "57P01", "57P02", "57P03" -> build(
            status = HttpStatus.SERVICE_UNAVAILABLE,
            request = request,
            message = normalizeSqlMessage(exception.message)
        )

        else -> build(
            status = HttpStatus.CONFLICT,
            request = request,
            message = normalizeSqlMessage(exception.message)
        )
    }

    @ExceptionHandler(DataAccessException::class)
    fun handleDataAccess(
        exception: DataAccessException,
        request: HttpServletRequest
    ): ResponseEntity<ApiErrorResponseBody> = when (extractSqlState(exception)) {
        "23505", "23503", "23514", "22001", "22003", "22007", "22008" -> build(
            status = HttpStatus.CONFLICT,
            request = request,
            message = normalizeSqlMessage(exception.message)
        )

        "08000", "08001", "08003", "08004", "08006", "57P01", "57P02", "57P03" -> build(
            status = HttpStatus.SERVICE_UNAVAILABLE,
            request = request,
            message = normalizeSqlMessage(exception.message)
        )

        null -> if (isConnectivityFailure(exception)) {
            build(
                status = HttpStatus.SERVICE_UNAVAILABLE,
                request = request,
                message = normalizeSqlMessage(exception.message)
            )
        } else {
            build(
                status = HttpStatus.CONFLICT,
                request = request,
                message = normalizeSqlMessage(exception.message)
            )
        }

        else -> build(
            status = HttpStatus.CONFLICT,
            request = request,
            message = normalizeSqlMessage(exception.message)
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleUnexpected(
        exception: Exception,
        request: HttpServletRequest
    ): ResponseEntity<ApiErrorResponseBody> = if (isConnectivityFailure(exception)) {
        build(
            status = HttpStatus.SERVICE_UNAVAILABLE,
            request = request,
            message = normalizeSqlMessage(exception.message)
        )
    } else if (isBusinessRuleFailure(exception) || isWriteRequest(request)) {
        build(
            status = HttpStatus.CONFLICT,
            request = request,
            message = normalizeSqlMessage(exception.message)
        )
    } else {
        build(
            status = HttpStatus.INTERNAL_SERVER_ERROR,
            request = request,
            message = "Неожиданная ошибка. Проверьте логи приложения и состояние инфраструктуры."
        )
    }

    private fun build(
        status: HttpStatus,
        request: HttpServletRequest,
        message: String
    ): ResponseEntity<ApiErrorResponseBody> = ResponseEntity
        .status(status)
        .body(
            ApiErrorResponseBody(
                timestamp = OffsetDateTime.now(),
                status = status.value(),
                error = statusTitle(status),
                message = message
            )
        )

    private fun extractSqlState(exception: Throwable): String? = when (exception) {
        is UncategorizedSQLException -> exception.sqlException?.sqlState
        is SQLException -> exception.sqlState
        else -> exception.cause?.let(::extractSqlState)
    }

    private fun normalizeSqlMessage(raw: String?): String = raw
        ?.trim()
        ?.takeIf(String::isNotBlank)
        ?.let(::translateSqlMessage)
        ?: "Операция не выполнена. Проверьте входные данные и ограничения БД."

    private fun isConnectivityFailure(exception: Throwable): Boolean = when (exception) {
        is CannotGetJdbcConnectionException -> true
        is DataAccessResourceFailureException -> true
        is CannotCreateTransactionException -> true
        is SQLTimeoutException -> true
        is SQLTransientConnectionException -> true
        is SQLNonTransientConnectionException -> true
        else -> exception.cause?.let(::isConnectivityFailure) == true
    }

    private fun isBusinessRuleFailure(exception: Throwable): Boolean =
        exception.message?.let(::containsBusinessRuleSignature) == true ||
            exception.cause?.let(::isBusinessRuleFailure) == true

    private fun containsBusinessRuleSignature(message: String): Boolean = listOf(
        "department.name must not be empty",
        "branch.name must not be empty",
        "branch.address must not be empty",
        "supplier.company_name must not be empty",
        "check_in must be <= check_out",
        "price must be non-negative",
        "duplicate key value violates unique constraint",
        "violates foreign key constraint",
        "violates check constraint",
        "value too long",
        "invalid input syntax"
    ).any { message.contains(it, ignoreCase = true) }

    private fun isWriteRequest(request: HttpServletRequest): Boolean = request.method in setOf(
        "POST",
        "PUT",
        "PATCH",
        "DELETE"
    )

    private fun translateSqlMessage(message: String): String = when {
        message.contains("department.name must not be empty", ignoreCase = true) ->
            "Название департамента не может быть пустым."

        message.contains("branch.name must not be empty", ignoreCase = true) ->
            "Название филиала не может быть пустым."

        message.contains("branch.address must not be empty", ignoreCase = true) ->
            "Адрес филиала не может быть пустым."

        message.contains("supplier.company_name must not be empty", ignoreCase = true) ->
            "Название поставщика не может быть пустым."

        message.contains("check_in must be <= check_out", ignoreCase = true) ->
            "Дата заезда должна быть меньше или равна дате выезда."

        message.contains("price must be non-negative", ignoreCase = true) ->
            "Цена номера не может быть отрицательной."

        message.contains("duplicate key value violates unique constraint", ignoreCase = true) ->
            "Запись с такими ключевыми значениями уже существует."

        message.contains("violates foreign key constraint", ignoreCase = true) ->
            "Нарушена ссылочная целостность. Проверьте идентификаторы связанных сущностей."

        message.contains("violates check constraint", ignoreCase = true) ->
            "Нарушено бизнес-ограничение данных. Проверьте значения полей."

        message.contains("value too long", ignoreCase = true) ->
            "Слишком длинное значение поля. Сократите размер входных данных."

        message.contains("invalid input syntax", ignoreCase = true) ->
            "Некорректный формат входных данных."

        message.contains("could not connect", ignoreCase = true) ->
            "Сервис временно недоступен. Проверьте доступность базы данных филиала."

        else -> "Операция не выполнена. Проверьте входные данные и ограничения БД."
    }

    private fun statusTitle(status: HttpStatus): String = when (status) {
        HttpStatus.BAD_REQUEST -> "Некорректный запрос"
        HttpStatus.NOT_FOUND -> "Не найдено"
        HttpStatus.CONFLICT -> "Конфликт данных"
        HttpStatus.SERVICE_UNAVAILABLE -> "Сервис недоступен"
        HttpStatus.INTERNAL_SERVER_ERROR -> "Внутренняя ошибка"
        else -> status.reasonPhrase
    }
}
