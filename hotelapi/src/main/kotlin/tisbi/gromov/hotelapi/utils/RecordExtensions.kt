package tisbi.gromov.hotelapi.utils

import org.jooq.Record

fun <R : Record> R?.orNotFound(message: String): R =
    this ?: throw DatabaseRecordNotFoundException(message)