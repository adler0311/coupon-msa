-- Insert 4 available coupons
INSERT INTO coupon_microservice.coupon (name, max_issuance_count, usage_start_at, usage_exp_at, days_before_exp, discount_amount, discount_type)
VALUES ('Coupon 1', 1000, '2023-05-01 00:00:00', '2023-05-31 23:59:59', 30, 10, 'VALUE');

INSERT INTO coupon_microservice.coupon (name, max_issuance_count, usage_start_at, usage_exp_at, days_before_exp, discount_amount, discount_type)
VALUES ('Coupon 2', 500, '2023-05-01 00:00:00', '2023-05-15 23:59:59', 14, 20, 'VALUE');

INSERT INTO coupon_microservice.coupon (name, max_issuance_count, usage_start_at, usage_exp_at, days_before_exp, discount_amount, discount_type)
VALUES ('Coupon 3', 1000, '2023-05-01 00:00:00', '2023-05-31 23:59:59', 30, 5, 'VALUE');

INSERT INTO coupon_microservice.coupon (name, max_issuance_count, usage_start_at, usage_exp_at, days_before_exp, discount_amount, discount_type)
VALUES ('Coupon 4', 2000, '2023-05-01 00:00:00', '2023-06-30 23:59:59', 60, 15, 'VALUE');

-- Insert 6 expired coupons
INSERT INTO coupon_microservice.coupon (name, max_issuance_count, usage_start_at, usage_exp_at, days_before_exp, discount_amount, discount_type)
VALUES ('Expired Coupon 1', 1000, '2023-01-01 00:00:00', '2023-01-31 23:59:59', 30, 10, 'VALUE');
INSERT INTO coupon_microservice.coupon (name, max_issuance_count, usage_start_at, usage_exp_at, days_before_exp, discount_amount, discount_type)
VALUES ('Expired Coupon 2', 1000, '2023-01-01 00:00:00', '2023-01-31 23:59:59', 30, 10, 'VALUE');
INSERT INTO coupon_microservice.coupon (name, max_issuance_count, usage_start_at, usage_exp_at, days_before_exp, discount_amount, discount_type)
VALUES ('Expired Coupon 3', 1000, '2023-01-01 00:00:00', '2023-01-31 23:59:59', 30, 10, 'VALUE');
INSERT INTO coupon_microservice.coupon (name, max_issuance_count, usage_start_at, usage_exp_at, days_before_exp, discount_amount, discount_type)
VALUES ('Expired Coupon 4', 1000, '2023-01-01 00:00:00', '2023-01-31 23:59:59', 30, 10, 'VALUE');
INSERT INTO coupon_microservice.coupon (name, max_issuance_count, usage_start_at, usage_exp_at, days_before_exp, discount_amount, discount_type)
VALUES ('Expired Coupon 5', 1000, '2023-01-01 00:00:00', '2023-01-31 23:59:59', 30, 10, 'VALUE');
INSERT INTO coupon_microservice.coupon (name, max_issuance_count, usage_start_at, usage_exp_at, days_before_exp, discount_amount, discount_type)
VALUES ('Expired Coupon 6', 1000, '2023-01-01 00:00:00', '2023-01-31 23:59:59', 30, 10, 'VALUE');

