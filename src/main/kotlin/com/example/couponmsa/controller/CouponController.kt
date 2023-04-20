package com.example.couponmsa.controller

import com.example.couponmsa.domain.Coupon
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
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@Tag(name = "Coupon", description = "Coupon REST Controller")
@RestController
@RequestMapping(path = ["/api/v1/coupons"])
class CouponController(private val couponService: CouponService) {
    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        method = "createCoupon"
    )
    suspend fun createCoupon(@Valid @RequestBody req: CreateCouponDto) =
        withTimeout(timeOutMillis) {
            ResponseEntity.
            status(HttpStatus.CREATED).
            body(couponService.createCoupon(Coupon.of(req)).
            toSuccessHttpResponse()).
            also { log.info("created coupon: $it")}
        }

    companion object {
        private val log = LoggerFactory.getLogger(CouponController::class.java)
        private const val timeOutMillis = 5000L
    }
}