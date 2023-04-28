package com.example.couponmsa.service

import com.example.couponmsa.domain.Coupon
import com.example.couponmsa.domain.UserCoupon
import com.example.couponmsa.repository.CouponRepository
import com.example.couponmsa.repository.UserCouponRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import javax.validation.Valid

@Service
class CouponService(
    private val couponRepository: CouponRepository,
    private val userCouponRepository: UserCouponRepository
) {
    @Transactional
    suspend fun createCoupon(@Valid coupon: Coupon): Coupon =
        withContext(Dispatchers.IO) {
            couponRepository.save(coupon)
        }

    suspend fun updateCoupon(couponUpdateData: Coupon, couponId: Long): Coupon =
        withContext(Dispatchers.IO) {
            val existingCoupon = couponRepository.findById(couponId)
                ?: throw CouponNotFound("coupon not found with id: ${couponId}")

            existingCoupon.update(couponUpdateData)
            couponRepository.save(existingCoupon)
        }


    @Transactional
    suspend fun issueCoupon(@Valid userCoupon: UserCoupon): UserCoupon =
        withContext(Dispatchers.IO) {
            val existingCoupon = couponRepository.findById(userCoupon.couponId)
                ?: throw CouponNotFound("coupon not found with id: ${userCoupon.couponId}")
            val existingIssuedCoupon = userCouponRepository.findByCouponIdAndUserId(userCoupon.couponId, userCoupon.userId)

            validateIssuable(existingCoupon, existingIssuedCoupon)

            userCoupon.setExpiredAt(existingCoupon)
            existingCoupon.incrementIssuedCount()

            couponRepository.save(existingCoupon)
            userCouponRepository.save(userCoupon)
        }

    private fun validateIssuable(existingCoupon: Coupon, existingIssuedCoupon: UserCoupon?) {
        if (existingCoupon.isOutOfStock()) {
            throw CouponRunOutOfStock()
        }

        if (existingIssuedCoupon == null) {
            return
        }

        val expAfterIssued: LocalDateTime = existingIssuedCoupon.createdAt!!.plusDays(existingCoupon.daysBeforeExp.toLong())
        val expDateTime = minOf(expAfterIssued, existingCoupon.usageExpAt)

        if (LocalDateTime.now() < expDateTime) {
            throw CouponAlreadyIssued()
        }
    }

    suspend fun getCoupon(couponId: Long): Coupon =
        withContext(Dispatchers.IO) {
            couponRepository.findById(couponId) ?: throw CouponNotFound("coupon not found for id: ${couponId}")
        }

}

class CouponNotFound(message: String): RuntimeException(message)
class CouponRunOutOfStock(message: String = "out of stock"): RuntimeException(message)

class CouponAlreadyIssued(message: String = "already issued coupon"): RuntimeException(message)