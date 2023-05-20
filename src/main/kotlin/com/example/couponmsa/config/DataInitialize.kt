package com.example.couponmsa.config

import com.example.couponmsa.domain.Coupon
import com.example.couponmsa.domain.DiscountType
import com.example.couponmsa.service.CouponService
import kotlinx.coroutines.runBlocking
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime


@Configuration
class DataInitialize {

    @Bean
    fun addCouponForTest(couponService: CouponService) = CommandLineRunner {
        runBlocking {

            couponService.createCoupon(
                Coupon(id=5L,
                    name="Test coupon",
                    maxIssuanceCount = 7_000,
                    usageStartAt = LocalDateTime.now(),
                    usageExpAt = LocalDateTime.now().plusDays(5),
                    daysBeforeExp = 14,
                    discountAmount = 90,
                    discountType = DiscountType.RATE,
                    issuedCount = 0))
        }
    }
}