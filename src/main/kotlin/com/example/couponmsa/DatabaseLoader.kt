package com.example.couponmsa


import com.example.couponmsa.domain.UserCoupon
import com.example.couponmsa.repository.UserCouponRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.kotlin.core.publisher.toFlux
import java.time.LocalDateTime

@Component
class DatabaseLoader(
    private val userCouponRepository: UserCouponRepository
) : CommandLineRunner {
    private val ioScope = CoroutineScope(Dispatchers.IO)


    override fun run(vararg args: String?) {
        ioScope.launch {
            createAndSaveUserCoupons()
        }
    }
    private suspend fun createAndSaveUserCoupons() {
        // Generate 1,000,000 user coupons
        val userCoupons = List(1_000_000) { i ->
            UserCoupon(
                userId = (i + 1).toLong(),
                couponId = ((i + 1) % 4 + 1).toLong(),
                expiredAt = LocalDateTime.now().plusDays(100)
            )
        }.asFlow()

        // Save user coupons to the database
        userCouponRepository.saveAll(userCoupons).collect()

        println("Finished inserting 1,000,000 user coupons")
    }
}