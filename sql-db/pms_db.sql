SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for products
-- ----------------------------
DROP TABLE IF EXISTS `products`;
CREATE TABLE `products`  (
  `product_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `cover_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品封面',
  `merchant_id` int(0) NOT NULL COMMENT '商户id-外键',
  `product_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名',
  `price` decimal(10, 2) NOT NULL COMMENT '价格',
  `order_count` int(0) NOT NULL DEFAULT 0 COMMENT '下单人数',
  `inventory` int(0) NOT NULL DEFAULT 0 COMMENT '库存',
  `creation_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '商品创建时间',
  `status_code` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态码（0代表已删除，1代表正常）',
  PRIMARY KEY (`product_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of products
-- ----------------------------
INSERT INTO `products` VALUES (1, 'test_cover_01.jpg', 1, '商品A', 99.99, 102, 500, '2024-01-29 23:15:02', 1);
INSERT INTO `products` VALUES (2, 'test_cover_02.png', 2, '商品B', 199.99, 78, 200, '2024-01-29 23:15:02', 1);
INSERT INTO `products` VALUES (3, 'test_cover_03.gif', 1, '商品C', 49.99, 305, 999, '2024-01-29 23:15:02', 1);
INSERT INTO `products` VALUES (4, 'test_cover_04.jpeg', 3, '商品D', 599.99, 25, 150, '2024-01-29 23:15:02', 1);
INSERT INTO `products` VALUES (5, 'test_cover_05.bmp', 4, '商品E（已删除）', 129.99, 0, 0, '2024-01-29 23:15:02', 0);
INSERT INTO `products` VALUES (6, 'test_cover_06.png', 5, '商品F', 149.99, 0, 0, '2024-01-29 23:15:02', 1);

SET FOREIGN_KEY_CHECKS = 1;
