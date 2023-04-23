package com.example.couponmsa.e2e

import com.example.couponmsa.domain.Coupon
import com.example.couponmsa.domain.DiscountType
import com.example.couponmsa.domain.UserCoupon
import com.example.couponmsa.service.CouponService
import com.example.couponmsa.service.UseUserCouponDto
import com.example.couponmsa.service.UserCouponService
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import java.time.LocalDateTime

class UserCouponE2eTest(
    @Autowired private var userCouponService: UserCouponService,
    @Autowired private var couponService: CouponService
    ): AbstractE2eTest() {
    @Test
    fun `getUserCoupons should return list of user coupons`(): Unit = runBlocking {
        // given
        // create coupon
        val createdCoupon: Coupon = couponService.createCoupon(
            Coupon(
                daysBeforeExp = 1,
                discountAmount = 10,
                discountType = DiscountType.RATE,
                issuedCount = 1,
                usageExpAt = LocalDateTime.now().plusDays(3),
                usageStartAt = LocalDateTime.now(),
                name = "Test Coupon"))

        // issue coupon
        val userId: Long = 1
        couponService.issueCoupon(
            UserCoupon(
                couponId = createdCoupon.id!!,
                userId = userId,
            )
        )

        // when, then
        webTestClient.get()
            .uri("/api/v1/users/{userId}/coupons", userId)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.total").isEqualTo(1)
            .jsonPath("$.page").isEqualTo(1)
            .jsonPath("$.items[0].userId").isEqualTo(userId)
            .jsonPath("$.items[0].couponId").isEqualTo(1)
            .jsonPath("$.items[0].couponName").isEqualTo("Test Coupon")
            .jsonPath("$.items[0].discountType").isEqualTo(DiscountType.RATE.toString())
            .jsonPath("$.items[0].discountAmount").isEqualTo(10)
    }


    @ParameterizedTest
    @MethodSource("useUserCouponDataProvider")
    fun `useUserCoupon should return isUsed`(usageStatus: Boolean): Unit = runBlocking {
        // given
        // create coupon
        val createdCoupon: Coupon = couponService.createCoupon(
            Coupon(
                daysBeforeExp = 1,
                discountAmount = 10,
                discountType = DiscountType.RATE,
                issuedCount = 1,
                usageExpAt = LocalDateTime.now().plusDays(3),
                usageStartAt = LocalDateTime.now(),
                name = "Test Coupon"))

        // issue coupon
        val userId: Long = 1
        couponService.issueCoupon(
            UserCoupon(
                couponId = createdCoupon.id!!,
                userId = userId,
            )
        )

        // when, then
        webTestClient.patch()
            .uri("/api/v1/users/{userId}/coupons/{couponId}/usage", userId, createdCoupon.id)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(UseUserCouponDto(usageStatus = usageStatus))
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.isUsed").isEqualTo(usageStatus)
    }



    companion object {
        @JvmStatic
        fun useUserCouponDataProvider() = listOf(
            Arguments.of(true),
            Arguments.of(false),

        )
    }


}