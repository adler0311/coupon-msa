package com.example.couponmsa.repository

import com.example.couponmsa.domain.DiscountType
import com.example.couponmsa.domain.UserCoupon
import com.example.couponmsa.service.UserCouponDto
import io.r2dbc.spi.Row
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.withContext
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class UserCouponMySQLRepository(
        private val databaseClient: DatabaseClient,
) {
    suspend fun getUserCoupons(userId: Long, pageable: Pageable): Page<UserCouponDto> =
        withContext(Dispatchers.IO) {
            val userCouponList = async {
                databaseClient.sql(
                    "SELECT * FROM coupon_microservice.user_coupon uc " +
                            "JOIN coupon_microservice.coupon c " +
                            "ON uc.coupon_id = c.coupon_id " +
                            "WHERE user_id =:userId AND expired_at > :now " +
                            "LIMIT :limit " +
                            "OFFSET :offset")
                    .bind("userId", userId)
                    .bind("now", LocalDateTime.now())
                    .bind("limit", pageable.pageSize)
                    .bind("offset", pageable.offset)
                    .map {row -> mapRowToUserCouponDto(row)}
                    .all()
                    .asFlow()
                    .toList()
            }

            val totalCount = async {
                    databaseClient.sql("SELECT count(user_coupon.id) as total FROM coupon_microservice.user_coupon WHERE user_id =:userId AND expired_at > :now")
                        .bind("userId", userId)
                        .bind("now", LocalDateTime.now())
                        .map {row -> row.get("total", Long::class.java)}
                        .one()
                        .awaitFirst()
            }

            PageImpl(userCouponList.await(), pageable, totalCount.await()!!)
        }

    private fun mapRowToUserCouponDto(row: Row): UserCouponDto {
        return UserCouponDto(
            userId = row.get("user_id", Long::class.java)!!,
            couponId = row.get("coupon_id", Long::class.java)!!,
            issuedAt = row.get("created_at", LocalDateTime::class.java)!!,
            discountAmount = row.get("discount_amount", Long::class.java)!!,
            discountType = DiscountType.valueOf(row.get("discount_type", String::class.java)!!),
            couponName = row.get("name", String::class.java)!!,
            usageStartAt = row.get("usage_start_at", LocalDateTime::class.java)!!,
            usedAt = row.get("used_at", LocalDateTime::class.java),
            expiredAt = row.get("expired_at", LocalDateTime::class.java)!!
        )
    }
}