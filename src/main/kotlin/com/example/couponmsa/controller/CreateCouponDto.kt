package com.example.couponmsa.controller

import com.example.couponmsa.domain.DiscountType
import java.time.LocalDateTime

data class CreateCouponDto(
    val name: String,
    val maxIssuanceCount: Int,
    val usageStartDt: LocalDateTime,
    val usageExpDt: LocalDateTime,
    val daysBeforeExp: Int,
    val discountAmount: Int,
    val discountType: DiscountType,
)