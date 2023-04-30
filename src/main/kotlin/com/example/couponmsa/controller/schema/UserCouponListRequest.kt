package com.example.couponmsa.controller.schema

import com.example.couponmsa.service.UserCouponDto
import org.springframework.data.domain.Page

class UserCouponListRequest(
    val total: Long,
    val page: Int,
    val items: MutableList<UserCouponDto>
) {
    companion object {
        fun of(userCoupons: Page<UserCouponDto>): Any {
            return UserCouponListRequest(
                items = userCoupons.content,
                total = userCoupons.totalElements,
                page = userCoupons.number + 1
            )
        }
    }
}
