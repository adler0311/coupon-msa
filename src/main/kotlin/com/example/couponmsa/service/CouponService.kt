package com.example.couponmsa.service

import com.example.couponmsa.domain.Coupon
import com.example.couponmsa.repository.CouponRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.validation.Valid

@Service
class CouponService(
    private val couponRepository: CouponRepository
) {
    @Transactional
    suspend fun createCoupon(@Valid coupon: Coupon): Coupon =
        withContext(Dispatchers.IO) {
            couponRepository.save(coupon)
        }
}