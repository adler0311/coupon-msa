CREATE SCHEMA IF NOT EXISTS coupon_microservice;

CREATE TABLE IF NOT EXISTS coupon_microservice.coupon (
    coupon_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    max_issuance_count INTEGER,
    usage_start_dt TIMESTAMP NOT NULL,
    usage_exp_dt TIMESTAMP NOT NULL,
    days_before_exp INTEGER NOT NULL,
    discount_amount INTEGER NOT NULL,
    discount_type VARCHAR(255) NOT NULL
    );
