package com.example.couponmsa.e2e

import dev.miku.r2dbc.mysql.MySqlConnectionConfiguration
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.containers.MySQLContainerProvider
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers


//@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
@Testcontainers
abstract class AbstractE2eTest {
    @Autowired
    protected lateinit var webTestClient: WebTestClient

    private fun getConnectionFactory(): ConnectionFactory {
        return ConnectionFactories.get(ConnectionFactoryOptions.builder()
            .option(ConnectionFactoryOptions.DRIVER, "mysql")
            .option(ConnectionFactoryOptions.HOST, mySQLContainer.host)
            .option(ConnectionFactoryOptions.PORT, mySQLContainer.firstMappedPort)
            .option(ConnectionFactoryOptions.DATABASE, mySQLContainer.databaseName)
            .option(ConnectionFactoryOptions.USER, mySQLContainer.username)
            .option(ConnectionFactoryOptions.PASSWORD, mySQLContainer.password)
            .build())
    }

    @BeforeEach
    fun resetTables() {
        val connectionFactory = getConnectionFactory()
        val userCouponTbl = DatabaseClient.create(connectionFactory)
            .sql("SHOW TABLES LIKE 'user_coupon';")
            .fetch()
            .all()
            .collectList()
            .block()!!.isNotEmpty()

        if (userCouponTbl) {
            DatabaseClient.create(connectionFactory).sql("DELETE FROM user_coupon;").then().block()
            DatabaseClient.create(connectionFactory).sql("ALTER TABLE user_coupon AUTO_INCREMENT = 1;").then().block()
        }

        val couponTbl = DatabaseClient.create(connectionFactory)
            .sql("SHOW TABLES LIKE 'coupon';")
            .fetch()
            .all()
            .collectList()
            .block()!!.isNotEmpty()
        if (couponTbl) {
            DatabaseClient.create(connectionFactory).sql("DELETE FROM coupon;").then().block()
            DatabaseClient.create(connectionFactory).sql("ALTER TABLE coupon AUTO_INCREMENT = 1;").then().block()

        }
    }


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