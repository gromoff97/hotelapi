package tisbi.gromov.hotelapi.branch.room

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
@RequestMapping("/api/v1/branches/{branch}/rooms")
class BranchRoomController(
    private val branchRoomService: BranchRoomService
) {
    @GetMapping
    fun list(
        @PathVariable branch: String,
        @RequestParam(required = false) type: String?,
        @RequestParam(required = false) maxPrice: BigDecimal?
    ): List<BranchRoomDto> = branchRoomService.list(branch, type, maxPrice)

    @GetMapping("/{roomId}")
    fun get(
        @PathVariable branch: String,
        @PathVariable roomId: Int
    ): BranchRoomDto = branchRoomService.get(branch, roomId)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @PathVariable branch: String,
        @RequestBody request: BranchRoomRequest
    ): BranchRoomDto = branchRoomService.create(branch, request)

    @DeleteMapping("/{roomId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @PathVariable branch: String,
        @PathVariable roomId: Int
    ) {
        branchRoomService.delete(branch, roomId)
    }

    @PutMapping("/{roomId}")
    fun update(
        @PathVariable branch: String,
        @PathVariable roomId: Int,
        @RequestBody request: BranchRoomRequest
    ): BranchRoomDto = branchRoomService.update(branch, roomId, request)
}