package com.example.couponmsa.domain

import com.example.couponmsa.controller.IssueCouponDto
import com.example.couponmsa.controller.SuccessUserCouponResponse
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import javax.validation.constraints.AssertTrue

@Table(schema = "coupon_microservice", name="user_coupon")
data class UserCoupon(
    @Column("id") @Id var id: Long? = null,
    @Column("user_id") var userId: Long,
    @Column("coupon_id") var couponId: Long,

    @CreatedDate
    @Column("created_at") var createdAt: LocalDateTime? = null,
    @Column("used_at") var usedAt: LocalDateTime? = null

    ) {

    companion object
}

fun UserCoupon.Companion.of(request: IssueCouponDto, couponId: Long): UserCoupon {
    return UserCoupon(
        id=null,
        userId=request.userId,
        couponId = couponId
    )
}

fun UserCoupon.toSuccessHttpResponse(): SuccessUserCouponResponse {
    return SuccessUserCouponResponse(
        id=this.id
    )

}
