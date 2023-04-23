package com.example.couponmsa.e2e

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.containers.MySQLContainerProvider
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
@Testcontainers
abstract class AbstractE2eTest {
    @Autowired
    protected lateinit var webTestClient: WebTestClient

    companion object {
        @Container
        val mySQLContainer = MySQLContainerProvider().newInstance("8.0.24").apply {
            withUsername("test")
            withPassword("test_password")
            withDatabaseName("coupon_microservice")
            start()
        }

        @JvmStatic
        @DynamicPropertySource
        fun setProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.r2dbc.url") { "r2dbc:mysql://${mySQLContainer.host}:${mySQLContainer.firstMappedPort}/${mySQLContainer.databaseName}" }
            registry.add("spring.r2dbc.username", mySQLContainer::getUsername)
            registry.add("spring.r2dbc.password", mySQLContainer::getPassword)
        }
    }

}