package com.example.couponmsa.e2e


import com.example.couponmsa.controller.CreateCouponDto
import com.example.couponmsa.controller.IssueCouponDto
import com.example.couponmsa.controller.UpdateCouponDto
import com.example.couponmsa.domain.Coupon
import com.example.couponmsa.domain.DiscountType
import com.example.couponmsa.repository.UserCouponRepository
import com.example.couponmsa.service.CouponService
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import java.time.LocalDateTime

class CouponE2eTest(
    @Autowired private var couponService: CouponService,
    @Autowired private var userCouponRepository: UserCouponRepository
    ): AbstractE2eTest() {


    @Test
    fun `should create a coupon and retrieve id of it`() {
        // given
        val createCouponDto = CreateCouponDto(
            name = "Test Coupon",
            maxIssuanceCount = 100,
            usageStartAt = LocalDateTime.now(),
            usageExpAt = LocalDateTime.now().plusDays(10),
            daysBeforeExp = 10,
            discountAmount = 10,
            discountType = DiscountType.VALUE,
        )

        // when, then
        webTestClient.post().uri("/api/v1/coupons/")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(createCouponDto)
            .exchange().expectStatus().isCreated
            .expectBody()
            .jsonPath("$.id").isEqualTo(1L)
    }

    @Test
    fun `should update the coupon and retrieve id of it`(): Unit = runBlocking {
        // given
        val aCoupon = Coupon(
            daysBeforeExp = 1,
            name = "test coupon",
            maxIssuanceCount = 100,
            usageStartAt = LocalDateTime.now(),
            usageExpAt = LocalDateTime.now().plusDays(2),
            discountType = DiscountType.VALUE,
            discountAmount = 1000,
            issuedCount = 0
        )
        val createdCoupon = couponService.createCoupon(aCoupon)

        // when
        val updateCouponDto = UpdateCouponDto(
            name = "Test Coupon updated",
            maxIssuanceCount = 1,
            usageStartAt = LocalDateTime.now(),
            usageExpAt = LocalDateTime.now().plusDays(10),
            daysBeforeExp = 10,
            discountAmount = 10,
            discountType = DiscountType.VALUE,
        )

        // when, then
        webTestClient.put().uri("/api/v1/coupons/${createdCoupon.id}")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(updateCouponDto)
            .exchange().expectStatus().isOk
            .expectBody()
            .jsonPath("$.id").isEqualTo(createdCoupon.id!!)

        val theCoupon: Coupon = couponService.getCoupon(createdCoupon.id!!)
        assertEquals(updateCouponDto.name, theCoupon.name)
    }
    @Test
    fun `should issue coupon to user and retrieve id of it`(): Unit = runBlocking{
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

        // when, then
        webTestClient.post().uri("/api/v1/coupons/{couponId}/issuance", createdCoupon.id)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(IssueCouponDto(userId=userId))
            .exchange().expectStatus().isCreated
            .expectBody()
            .jsonPath("$.id").isEqualTo(1L)

        val userCoupon = userCouponRepository.findByCouponIdAndUserId(couponId = createdCoupon.id!!, userId=userId)
        assertNotNull(userCoupon)
    }
}

