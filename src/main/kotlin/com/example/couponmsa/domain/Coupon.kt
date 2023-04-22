package com.example.couponmsa.domain

import com.example.couponmsa.controller.CreateCouponDto
import com.example.couponmsa.controller.SuccessCouponResponse
import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.v3.oas.annotations.Hidden
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import javax.validation.constraints.AssertTrue
import javax.validation.constraints.Min
import javax.validation.constraints.Size

@Table(schema = "coupon_microservice", name="coupon")
data class Coupon(
    @Column("coupon_id") @Id var id: Long? = null,
    @Column("name") val name: String,
    @get: Min(value = 1) @Column("max_issuance_count") val maxIssuanceCount: Int? = null,
    @Column("usage_start_dt") val usageStartDt: LocalDateTime,
    @Column("usage_exp_dt") val usageExpDt: LocalDateTime,
    @get: Min(value = 1) @Column("days_before_exp") val daysBeforeExp: Int,
    @Column("discount_amount") val discountAmount: Int,
    @Column("discount_type") val discountType: DiscountType,

    ) {



    @AssertTrue(message = "usageStartDt must be before usageExpDt")
    fun isValidUsagePeriod(): Boolean {
        return usageStartDt.isBefore(usageExpDt)
    }


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

    companion object
}

fun Coupon.Companion.of(request: CreateCouponDto): Coupon {
    return Coupon(
        id=null,
        name=request.name,
        maxIssuanceCount = request.maxIssuanceCount,
        usageStartDt = request.usageStartDt,
        usageExpDt = request.usageExpDt,
        daysBeforeExp = request.daysBeforeExp,
        discountAmount = request.discountAmount,
        discountType = request.discountType
    )
}

fun Coupon.toSuccessHttpResponse(): SuccessCouponResponse {
    return SuccessCouponResponse(
        id=this.id
    )

}
