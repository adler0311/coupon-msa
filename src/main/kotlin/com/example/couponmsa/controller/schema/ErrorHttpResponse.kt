package com.example.couponmsa.controller.schema

data class ErrorHttpResponse(
    val status: Int,
    val message: String,
    val timestamp: String
)
