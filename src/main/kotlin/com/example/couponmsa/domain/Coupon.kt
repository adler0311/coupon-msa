package com.example.couponmsa.domain

import com.example.couponmsa.controller.CreateCouponDto
import com.example.couponmsa.controller.SuccessCouponResponse
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table(schema = "coupon_microservice", name="coupon")
data class Coupon(
    @Column("coupon_id") @Id var id: Long? = null,
    @Column("name") val name: String,
    @Column("max_issuance_count") val maxIssuanceCount: Int?,
    @Column("usage_start_dt") val usageStartDt: LocalDateTime,
    @Column("usage_exp_dt") val usageExpDt: LocalDateTime,
    @Column("days_before_exp") val daysBeforeExp: Int,
    @Column("discount_amount") val discountAmount: Int,
    @Column("discount_type") val discountType: DiscountType,

) {
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
