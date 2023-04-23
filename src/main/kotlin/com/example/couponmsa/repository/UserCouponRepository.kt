package com.example.couponmsa.repository

import com.example.couponmsa.domain.Coupon
import com.example.couponmsa.domain.UserCoupon
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface UserCouponRepository: CoroutineSortingRepository<UserCoupon, Long> {
    suspend fun findByCouponIdAndUserId(couponId: Long, userId: Long): UserCoupon?

}