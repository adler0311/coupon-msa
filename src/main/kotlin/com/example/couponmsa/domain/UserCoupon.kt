package com.example.couponmsa.domain

import com.example.couponmsa.controller.IssueCouponDto
import com.example.couponmsa.controller.SuccessUserCouponResponse
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime


@Table(schema = "coupon_microservice", name="user_coupon")
data class UserCoupon(
    @Column("id") @Id var id: Long? = null,
    @Column("user_id") var userId: Long,

    @Column("coupon_id") var couponId: Long,

    @CreatedDate
    @Column("created_at") var createdAt: LocalDateTime? = null,
    @Column("used_at") var usedAt: LocalDateTime? = null,

    @Column("expired_at") var expiredAt: LocalDateTime? = null

    ) {
    fun setExpiredAt(existingCoupon: Coupon) {
        val now = LocalDateTime.now()
        expiredAt = minOf(existingCoupon.usageExpAt, now.plusDays(existingCoupon.daysBeforeExp.toLong()))
    }

    fun use(usageStatus: Boolean) {
        usedAt = if (usageStatus) {
            LocalDateTime.now()
        } else {
            null
        }
    }

    companion object
}

fun UserCoupon.Companion.of(request: IssueCouponDto, couponId: Long): UserCoupon {
    return UserCoupon(
        id=null,
        userId=request.userId,
        couponId = couponId,
    )
}

fun UserCoupon.toSuccessHttpResponse(): SuccessUserCouponResponse {
    return SuccessUserCouponResponse(
        id=this.id
    )

}
