package tisbi.gromov.hotelapi.utils

import tisbi.gromov.hotelapi.central.branch.CentralBranchDto
import tisbi.gromov.hotelapi.central.department.CentralDepartmentDto
import tisbi.gromov.hotelapi.central.staff.CentralStaffDto
import tisbi.gromov.hotelapi.central.supplier.CentralSupplierDto
import tisbi.gromov.hotelapi.central.supplier_branch.CentralSupplierBranchDto
import tisbi.gromov.hotelapi.jooq.central.tables.records.*

fun BranchRecord.toDto(): CentralBranchDto = CentralBranchDto(
    branchId = requireNotNull(this.branchId),
    name = requireNotNull(this.name),
    address = requireNotNull(this.address),
)

fun DepartmentRecord.toDto(): CentralDepartmentDto = CentralDepartmentDto(
    departmentId = requireNotNull(this.departmentId),
    name = requireNotNull(this.name),
)

fun StaffCentralRecord.toDto(): CentralStaffDto = CentralStaffDto(
    staffId = requireNotNull(this.staffId),
    name = requireNotNull(this.name),
    departmentId = requireNotNull(this.departmentId),
)

fun SupplierRecord.toDto(): CentralSupplierDto = CentralSupplierDto(
    supplierId = requireNotNull(this.supplierId),
    companyName = requireNotNull(this.companyName),
)

fun SupplierBranchRecord.toDto(): CentralSupplierBranchDto = CentralSupplierBranchDto(
    supplierId = requireNotNull(this.supplierId),
    branchId = requireNotNull(this.branchId),
)