package tisbi.gromov.hotelapi.central.department

import org.jooq.DSLContext
import org.jooq.impl.DSL.trueCondition
import org.springframework.stereotype.Repository
import tisbi.gromov.hotelapi.jooq.central.Tables.DEPARTMENT
import tisbi.gromov.hotelapi.jooq.central.tables.records.DepartmentRecord

@Repository
class CentralDepartmentRepository(private val ctx: DSLContext) {

    fun findAll(name: String?): List<DepartmentRecord> {
        val condition = if (name.isNullOrBlank()) trueCondition() else DEPARTMENT.NAME.likeIgnoreCase("%$name%")
        return ctx.selectFrom(DEPARTMENT)
            .where(condition)
            .orderBy(DEPARTMENT.DEPARTMENT_ID)
            .fetch()
    }

    fun findById(departmentId: Int): DepartmentRecord? =
        ctx.selectFrom(DEPARTMENT)
            .where(DEPARTMENT.DEPARTMENT_ID.eq(departmentId))
            .fetchOne()

    fun insert(name: String): DepartmentRecord =
        ctx.insertInto(DEPARTMENT)
            .set(DEPARTMENT.NAME, name)
            .returning()
            .fetchOne()!!

    fun update(departmentId: Int, name: String): DepartmentRecord? =
        ctx.update(DEPARTMENT)
            .set(DEPARTMENT.NAME, name)
            .where(DEPARTMENT.DEPARTMENT_ID.eq(departmentId))
            .returning()
            .fetchOne()

    fun delete(departmentId: Int): Int =
        ctx.deleteFrom(DEPARTMENT)
            .where(DEPARTMENT.DEPARTMENT_ID.eq(departmentId))
            .execute()
}