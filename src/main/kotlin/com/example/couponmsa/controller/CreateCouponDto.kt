package com.example.couponmsa.controller

import com.example.couponmsa.domain.DiscountType
import io.swagger.v3.oas.annotations.Hidden
import java.time.LocalDateTime
import javax.validation.constraints.AssertTrue
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

data class CreateCouponDto(
    @field: NotBlank
    val name: String,

    @get: Min(value = 1)  val maxIssuanceCount: Int? = null,

    val usageStartAt: LocalDateTime,

    val usageExpAt: LocalDateTime,

    @get: Min(value = 1) val daysBeforeExp: Int,

    val discountAmount: Int,

    val discountType: DiscountType,

    ) {
    @Hidden
    @AssertTrue(message = "usageStartAt must be before usageExpAt")
    fun isValidUsagePeriod(): Boolean {
        return usageStartAt.isBefore(usageExpAt)
    }

    @Hidden
    @AssertTrue(message = "discountAmount must be  0 between 100 when discountType is rate")
    fun isValidDiscountValue(): Boolean {
        if (discountType == DiscountType.VALUE) {
            return discountAmount > 0
        }

        if (discountType == DiscountType.RATE) {
            return discountAmount in 1..99
        }

        return true
    }
}