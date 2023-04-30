package com.example.couponmsa.service

import com.example.couponmsa.domain.Coupon
import com.example.couponmsa.domain.UserCoupon
import com.example.couponmsa.repository.CouponRepository
import com.example.couponmsa.repository.UserCouponRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import javax.validation.Valid

@Service
class CouponService(
    private val couponRepository: CouponRepository,
    private val userCouponRepository: UserCouponRepository,
    private val redisTemplate: RedisTemplate<String, Long>,
) {
    @Transactional
    suspend fun createCoupon(@Valid coupon: Coupon): Coupon =
        withContext(Dispatchers.IO) {
            val savedCoupon = couponRepository.save(coupon)
            setIssuanceCountInRedis(couponId=savedCoupon.id!!)
            savedCoupon
        }

    private fun setIssuanceCountInRedis(couponId: Long) {
        redisTemplate.opsForValue().set(getIssuanceCountRedisKey(couponId), 0L)
    }

    suspend fun updateCoupon(couponUpdateData: Coupon, couponId: Long): Coupon =
        withContext(Dispatchers.IO) {
            val existingCoupon = couponRepository.findById(couponId)
                ?: throw CouponNotFound("coupon not found with id: $couponId")

            existingCoupon.update(couponUpdateData)
            couponRepository.save(existingCoupon)
        }


    @Transactional
    suspend fun issueCoupon(@Valid userCoupon: UserCoupon): UserCoupon =
        withContext(Dispatchers.IO) {
            val existingCoupon = couponRepository.findById(userCoupon.couponId)
                ?: throw CouponNotFound("coupon not found with id: ${userCoupon.couponId}")
            val existingIssuedCoupon = userCouponRepository.findByUserIdAndCouponId(userCoupon.userId, userCoupon.couponId)

            validateIssuable(existingCoupon, existingIssuedCoupon)

            userCoupon.setExpiredAt(existingCoupon)
            incrementIssuedCountInRedis(existingCoupon.id!!)
            userCouponRepository.save(userCoupon)
//            UserCoupon(id=1L, couponId=1L, userId=1)
        }

    private fun incrementIssuedCountInRedis(couponId: Long) {
        redisTemplate.opsForValue().increment(getIssuanceCountRedisKey(couponId), 1)
    }

    private fun validateIssuable(existingCoupon: Coupon, existingIssuedCoupon: UserCoupon?) {
        val issuedCount = getIssuedCountFromRedis(existingCoupon.id!!)
        if (existingCoupon.maxIssuanceCount != null && issuedCount >= existingCoupon.maxIssuanceCount!!) {
            throw CouponRunOutOfStock()
        }

        if (existingIssuedCoupon == null) {
            return
        }

        val expAfterIssued: LocalDateTime = existingIssuedCoupon.createdAt!!.plusDays(existingCoupon.daysBeforeExp.toLong())
        val expDateTime = minOf(expAfterIssued, existingCoupon.usageExpAt)

        if (LocalDateTime.now() < expDateTime) {
            throw CouponAlreadyIssued("already issued coupon. userId: ${existingIssuedCoupon.userId}, couponId: ${existingIssuedCoupon.couponId}")
        }
    }

    private fun getIssuanceCountRedisKey(couponId: Long): String {
        return "coupon:$couponId:issuanceCount"
    }

    private fun getIssuedCountFromRedis(couponId: Long): Long {
        val result = redisTemplate.opsForValue().get(getIssuanceCountRedisKey(couponId))
        return result ?: 0L
    }

    suspend fun getCoupon(couponId: Long): Coupon =
        withContext(Dispatchers.IO) {
            couponRepository.findById(couponId) ?: throw CouponNotFound("coupon not found for id: $couponId")
        }

}

class CouponNotFound(message: String): RuntimeException(message)
class CouponRunOutOfStock(message: String = "out of stock"): RuntimeException(message)

class CouponAlreadyIssued(message: String): RuntimeException(message)