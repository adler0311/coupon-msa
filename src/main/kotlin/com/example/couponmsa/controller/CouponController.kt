package com.example.couponmsa.controller

import com.example.couponmsa.controller.schema.CreateCouponRequest
import com.example.couponmsa.controller.schema.IssueCouponRequest
import com.example.couponmsa.controller.schema.UpdateCouponRequest
import com.example.couponmsa.domain.Coupon
import com.example.couponmsa.domain.UserCoupon
import com.example.couponmsa.domain.of
import com.example.couponmsa.domain.toSuccessHttpResponse
import com.example.couponmsa.service.CouponService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.withTimeout
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Tag(name = "Coupon", description = "Coupon REST Controller")
@RestController
@RequestMapping(path = ["/api/v1/coupons"])
class CouponController(private val couponService: CouponService) {
    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        method = "createCoupon"
    )
    suspend fun createCoupon(@Valid @RequestBody req: CreateCouponRequest) =
        withTimeout(timeOutMillis) {
            ResponseEntity.
            status(HttpStatus.CREATED).
            body(couponService.createCoupon(Coupon.of(req)).
            toSuccessHttpResponse()).
            also { log.info("created coupon: $it")}
        }

    @PutMapping(path = ["/{couponId}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        method = "updateCoupon"
    )
    suspend fun updateCoupon(@Valid @RequestBody req: UpdateCouponRequest,
                             @PathVariable couponId: Long
                             ) =
        withTimeout(timeOutMillis) {
            ResponseEntity.
            status(HttpStatus.OK).
            body(couponService.updateCoupon(Coupon.of(req), couponId).
            toSuccessHttpResponse()).
            also { log.info("created coupon: $it")}
        }


    @PostMapping(path = ["/{couponId}/issuance"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(method="issueCoupon")
    suspend fun issueCoupon(@Valid @RequestBody req: IssueCouponRequest, @PathVariable couponId: Long) =
        withTimeout(timeOutMillis) {
            ResponseEntity.
            status(HttpStatus.CREATED).
            body(couponService.issueCoupon(UserCoupon.of(req, couponId)).
            toSuccessHttpResponse()).
            also { log.info("issued coupon: $it")}
        }

    companion object {
        private val log = LoggerFactory.getLogger(CouponController::class.java)
        private const val timeOutMillis = 10000L
    }
}