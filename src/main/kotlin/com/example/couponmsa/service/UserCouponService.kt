package com.example.couponmsa.service

import com.example.couponmsa.repository.UserCouponMySQLRepository
import com.example.couponmsa.repository.UserCouponRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class UserCouponService(private val userCouponRepository: UserCouponMySQLRepository) {
    suspend fun getUserCoupons(userId: Long, pageable: Pageable): Page<UserCouponDto> =
        withContext(Dispatchers.IO) {
            userCouponRepository.getUserCoupons(userId, pageable)
        }


}