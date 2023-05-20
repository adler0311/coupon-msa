CREATE SCHEMA IF NOT EXISTS coupon_microservice;

DROP TABLE if exists coupon_microservice.user_coupon;
DROP TABLE if exists coupon_microservice.coupon;

CREATE TABLE IF NOT EXISTS coupon_microservice.coupon (
    coupon_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    max_issuance_count INTEGER,
    usage_start_at TIMESTAMP NOT NULL,
    usage_exp_at TIMESTAMP NOT NULL,
    days_before_exp INTEGER NOT NULL,
    discount_amount INTEGER NOT NULL,
    discount_type VARCHAR(255) NOT NULL,
    issued_count INTEGER DEFAULT 0
    );

CREATE TABLE IF NOT EXISTS `coupon_microservice`.`user_coupon` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT(20) NOT NULL,
    `coupon_id` BIGINT(20) NOT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `used_at` DATETIME,
    `expired_at` DATETIME,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_user_coupon_coupon`
    FOREIGN KEY (`coupon_id`)
    REFERENCES `coupon_microservice`.`coupon` (`coupon_id`)
    ) ENGINE = InnoDB;


ALTER TABLE `coupon_microservice`.`user_coupon`
    ADD INDEX `ix_user_id_expired_at` (`user_id` ASC, `expired_at` DESC) VISIBLE;


ALTER TABLE `coupon_microservice`.`user_coupon`
    ADD INDEX `ix_user_id_coupon_id` (`user_id` ASC, `coupon_id` ASC) VISIBLE;
ALTER TABLE `coupon_microservice`.`user_coupon` ALTER INDEX `fk_user_coupon_coupon` INVISIBLE;
