package com.example.couponmsa.controller

data class ErrorHttpResponse(
    val status: Int,
    val message: String,
    val timestamp: String
)
