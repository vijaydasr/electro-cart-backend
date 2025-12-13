CREATE TABLE IF NOT EXISTS `t_orders`
(
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `order_number` VARCHAR(255) DEFAULT NULL,
    `sku_code` VARCHAR(255),
    `price` DECIMAL(19, 2),
    `quantity` INT(11),
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `username` VARCHAR(255),
    `email` VARCHAR(255),
    PRIMARY KEY (`id`)
);