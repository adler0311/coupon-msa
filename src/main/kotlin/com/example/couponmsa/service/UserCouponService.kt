package com.example.couponmsa.service

import com.example.couponmsa.repository.UserCouponMySQLRepository
import com.example.couponmsa.repository.UserCouponRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class UserCouponService(private val userCouponRepository: UserCouponMySQLRepository,
    private val userCouponIRepository: UserCouponRepository) {
    suspend fun getUserCoupons(userId: Long, pageable: Pageable): Page<UserCouponDto> =
        withContext(Dispatchers.IO) {
            userCouponRepository.getUserCoupons(userId, pageable)
        }

    suspend fun useCoupon(userId: Long, couponId: Long, usageStatus: Boolean): Boolean =
        withContext(Dispatchers.IO) {
            val existingIssuedCoupon = userCouponIRepository.findByCouponIdAndUserId(couponId, userId) ?: throw UserCouponNotFound("issued coupon not found with user_id: ${userId}, coupon_id: ${couponId}")
            existingIssuedCoupon.use(usageStatus)
            userCouponIRepository.save(existingIssuedCoupon)
            true
        }

    suspend fun deleteUserCoupon(userId: Long, couponId: Long): Boolean =
        withContext(Dispatchers.IO) {
            val existingIssuedCoupon = userCouponIRepository.findByCouponIdAndUserId(couponId, userId) ?: throw UserCouponNotFound("issued coupon not found with user_id: ${userId}, coupon_id: ${couponId}")
            userCouponIRepository.delete(existingIssuedCoupon)
            true
        }
}

class UserCouponNotFound(message: String) : RuntimeException(message)
