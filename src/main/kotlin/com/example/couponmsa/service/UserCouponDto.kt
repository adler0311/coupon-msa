package com.example.couponmsa.service

import com.example.couponmsa.domain.DiscountType
import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.v3.oas.annotations.Hidden
import org.springframework.data.annotation.Transient
import java.time.LocalDateTime

data class UserCouponDto(
    val userId: Long,
    val couponId: Long,
    val issuedAt: LocalDateTime,
    val usedAt: LocalDateTime?,
    val couponName: String,
    val usageStartAt: LocalDateTime,
    val discountType: DiscountType,
    val discountAmount: Long,
    val expiredAt: LocalDateTime
)