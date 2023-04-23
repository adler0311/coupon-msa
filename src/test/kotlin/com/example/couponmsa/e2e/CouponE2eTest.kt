package com.example.couponmsa.e2e


import com.example.couponmsa.controller.CreateCouponDto
import com.example.couponmsa.controller.SuccessCouponResponse
import com.example.couponmsa.domain.DiscountType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import java.time.LocalDateTime

class CouponE2eTest: AbstractE2eTest() {

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
        webTestClient.post().uri("/api/v1/coupons")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(createCouponDto)
            .exchange().expectStatus().isCreated
            .expectBody()
            .jsonPath("$.id").isEqualTo(1L)
    }
}

