package com.example.couponmsa.domain

import com.example.couponmsa.controller.schema.CreateCouponRequest
import com.example.couponmsa.controller.schema.SuccessCouponResponse
import com.example.couponmsa.controller.schema.UpdateCouponRequest
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import javax.validation.constraints.AssertTrue
import javax.validation.constraints.Min

@Table(schema = "coupon_microservice", name="coupon")
data class Coupon(
    @Column("coupon_id") @Id var id: Long? = null,
    @Column("name") var name: String,
    @get: Min(value = 1) @Column("max_issuance_count") var maxIssuanceCount: Int? = null,
    @Column("usage_start_at") var usageStartAt: LocalDateTime,
    @Column("usage_exp_at") var usageExpAt: LocalDateTime,
    @get: Min(value = 1) @Column("days_before_exp") var daysBeforeExp: Int,
    @Column("discount_amount") var discountAmount: Int,
    @Column("discount_type") var discountType: DiscountType,
    @Column("issued_count") var issuedCount: Int
    ) {

    @AssertTrue(message = "usageStartAt must be before usageExpAt")
    fun isValidUsagePeriod(): Boolean {
        return usageStartAt.isBefore(usageExpAt)
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

    fun isOutOfStock(): Boolean {
        if (maxIssuanceCount == null) {
            return false
        }

        return maxIssuanceCount == issuedCount
    }

    fun incrementIssuedCount() {
        issuedCount += 1
    }

    fun update(couponUpdateData: Coupon) {
        name = couponUpdateData.name
        maxIssuanceCount = couponUpdateData.maxIssuanceCount
        usageStartAt = couponUpdateData.usageStartAt
        usageExpAt = couponUpdateData.usageExpAt
        daysBeforeExp = couponUpdateData.daysBeforeExp
        discountAmount = couponUpdateData.discountAmount
        discountType = couponUpdateData.discountType
    }


    companion object {
    }
}

fun Coupon.Companion.of(request: CreateCouponRequest): Coupon {
    return Coupon(
        id=null,
        name=request.name,
        maxIssuanceCount = request.maxIssuanceCount,
        usageStartAt = request.usageStartAt,
        usageExpAt = request.usageExpAt,
        daysBeforeExp = request.daysBeforeExp,
        discountAmount = request.discountAmount,
        discountType = request.discountType,
        issuedCount = 0,
    )
}

fun Coupon.Companion.of(request: UpdateCouponRequest): Coupon {
    return Coupon(
        id=null,
        name=request.name,
        maxIssuanceCount = request.maxIssuanceCount,
        usageStartAt = request.usageStartAt,
        usageExpAt = request.usageExpAt,
        daysBeforeExp = request.daysBeforeExp,
        discountAmount = request.discountAmount,
        discountType = request.discountType,
        issuedCount = 0,
    )
}

fun Coupon.toSuccessHttpResponse(): SuccessCouponResponse {
    return SuccessCouponResponse(
        id=this.id
    )

}
