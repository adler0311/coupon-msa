package com.example.couponmsa.service


import com.example.couponmsa.domain.Coupon
import com.example.couponmsa.domain.UserCoupon
import com.example.couponmsa.repository.CouponRepository
import com.example.couponmsa.repository.UserCouponRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.data.redis.core.RedisOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.SessionCallback
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
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
            setMaxIssuanceCountInRedis(couponId=savedCoupon.id!!, maxIssuanceCount=savedCoupon.maxIssuanceCount)
            savedCoupon
        }

    private fun setMaxIssuanceCountInRedis(couponId: Long, maxIssuanceCount: Int?) {
        if (maxIssuanceCount != null) {
            redisTemplate.opsForValue().set(getMaxIssuanceCountRedisKey(couponId), maxIssuanceCount.toLong())
        }
    }

    private fun getMaxIssuanceCountRedisKey(couponId: Long): String {
        return "coupon:$couponId:maxIssuanceCount"
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
            validateIssuable(userCoupon.couponId, userCoupon.userId)
            userCouponRepository.save(userCoupon)
        }

    fun validateIssuable(existingCouponId: Long, issueUserId: Long) {
        val sessionCallback = object : SessionCallback<List<Any>> {
            override fun <K : Any, V : Any> execute(operations: RedisOperations<K, V>): List<Any> {
                operations.multi()
                getIssuedCountFromRedis(existingCouponId)
                getMaxIssuedCountFromRedis(existingCouponId)
                isIssuedUserFromRedis(existingCouponId, issueUserId)
                return operations.exec()
            }
        }

        val results: List<Any> = redisTemplate.execute(sessionCallback)
        val issuedCount = results[0] as Long
        val maxIssuedCount = results[1] as Long?
        val existingIssuedCoupon = results[2] as Long

        if (maxIssuedCount != null && issuedCount >= maxIssuedCount) {
            rollbackIssuedUser(existingCouponId, issueUserId)
            throw CouponRunOutOfStock()
        }

        if (existingIssuedCoupon == 0.toLong()) {
            throw CouponAlreadyIssued("already issued coupon. userId: ${issueUserId}, couponId: $existingCouponId")
        }


    }

    private fun rollbackIssuedUser(existingCouponId: Long, issueUserId: Long) {
        redisTemplate.opsForSet().remove(getCouponIssuedUsersRedisKey(existingCouponId), issueUserId)
    }

    private fun isIssuedUserFromRedis(existingCouponId: Long, issueUserId: Long): Boolean {
        val result = redisTemplate.opsForSet().add(getCouponIssuedUsersRedisKey(existingCouponId), issueUserId)
        return result == 0.toLong()
    }

    private fun getCouponIssuedUsersRedisKey(couponId: Long): String {
        return "coupon:$couponId:issuedUsers"
    }

    private fun getMaxIssuedCountFromRedis(couponId: Long): Long? {
        return redisTemplate.opsForValue().get(getMaxIssuanceCountRedisKey(couponId))
    }

    private fun getIssuanceCountRedisKey(couponId: Long): String {
        return "coupon:$couponId:issuanceCount"
    }

    private fun getIssuedCountFromRedis(couponId: Long): Long {
        val issueCount = redisTemplate.opsForSet().size(getCouponIssuedUsersRedisKey(couponId))
        return issueCount ?: 0L
    }

    suspend fun getCoupon(couponId: Long): Coupon =
        withContext(Dispatchers.IO) {
            couponRepository.findById(couponId) ?: throw CouponNotFound("coupon not found for id: $couponId")
        }

}

class CouponNotFound(message: String): RuntimeException(message)
class CouponRunOutOfStock(message: String = "out of stock"): RuntimeException(message)

class CouponAlreadyIssued(message: String): RuntimeException(message)