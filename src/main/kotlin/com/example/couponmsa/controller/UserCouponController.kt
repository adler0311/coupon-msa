package com.example.couponmsa.controller

import com.example.couponmsa.service.UseUserCouponDto
import com.example.couponmsa.service.UserCouponService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.withTimeout
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "User Coupon", description = "User Coupon REST Controller")
@RestController
@RequestMapping(path = ["/api/v1/users/{userId}/coupons"])
class UserCouponController(private val userCouponService: UserCouponService) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        method = "getUserCoupons"
    )
    suspend fun getUserCoupons(@PathVariable userId: Long,
                               @RequestParam(name="page", defaultValue = "1") page: Int,
                               @RequestParam(name="size", defaultValue = "5") size: Int,
                               ) =
        withTimeout(timeOutMills) {
            ResponseEntity.
            status(HttpStatus.OK).
            body(UserCouponListDto.of(userCouponService.getUserCoupons(userId, PageRequest.of(page-1, size)))).
            also { log.info("user coupons: $it")}
        }

    @PatchMapping(path = ["/coupons/{couponId}/usage"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        method = "useUserCoupon"
    )
    suspend fun useUserCoupon(@PathVariable userId: Long,
                              @PathVariable couponId: Long,
                              @RequestBody useUserCouponDto: UseUserCouponDto
    ) =
        withTimeout(timeOutMills) {
            ResponseEntity.
                    status(HttpStatus.OK).body(userCouponService.useCoupon(userId, couponId, useUserCouponDto.usageStatus)).also { log.info("coupon used: $it")}
        }


    companion object {
        private val log = LoggerFactory.getLogger(UserCouponController::class.java)
        private const val timeOutMills = 5000L
    }

}