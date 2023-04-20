package com.example.couponmsa.controller

import kotlinx.coroutines.withTimeout
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DefaultController {

    @GetMapping(path = ["/health"])
    suspend fun healthCheck(): ResponseEntity<String> = withTimeout(5000L) {
        ResponseEntity.status(HttpStatus.OK).body("OK")
    }
}