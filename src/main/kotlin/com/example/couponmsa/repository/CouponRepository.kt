package com.example.couponmsa.repository

import com.example.couponmsa.domain.Coupon
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface CouponRepository: CoroutineSortingRepository<Coupon, Long>