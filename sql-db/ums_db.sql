/*
 Navicat Premium Data Transfer

 Source Server         : 南社生产数据库
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : 39.108.67.16:3306
 Source Schema         : ums_db

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 30/01/2024 00:00:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for auth_credentials
-- ----------------------------
DROP TABLE IF EXISTS `auth_credentials`;
CREATE TABLE `auth_credentials`  (
  `credentials_id` int(0) NOT NULL AUTO_INCREMENT,
  `id_card_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '身份证',
  `phone_number` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`credentials_id`) USING BTREE,
  UNIQUE INDEX `uk_phone_number`(`phone_number`) USING BTREE,
  UNIQUE INDEX `uk_email`(`email`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of auth_credentials
-- ----------------------------
INSERT INTO `auth_credentials` VALUES (1, NULL, '13302361327', '1713471338@qq.com');
INSERT INTO `auth_credentials` VALUES (19, NULL, NULL, 'a13302361327@163.com');
INSERT INTO `auth_credentials` VALUES (20, '11212121212', '11212121212', 'atnibamaitay1@foxmail.com');
INSERT INTO `auth_credentials` VALUES (28, NULL, NULL, 'atnibamaitay@foxmail.com');

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `user_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '用户ID-主键',
  `user_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户名-默认值(应该是随机生成)',
  `user_avatar` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '头像-默认（固定默认头像）',
  `user_role` tinyint(0) NOT NULL DEFAULT 0 COMMENT '用户角色-0:平台管理员;1:机构管理员;2:老师;3:学生;(默认为0)',
  `user_introduction` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户简介',
  `user_location_province` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '省份',
  `user_location_city` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '城市',
  `user_location_region` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区域',
  `longitude` double NULL DEFAULT NULL COMMENT '经度',
  `latitude` double NULL DEFAULT NULL COMMENT '纬度',
  `login_last_time` datetime(0) NULL DEFAULT NULL COMMENT '最后一次上线时间',
  `off_line_last_time` datetime(0) NULL DEFAULT NULL COMMENT '最后一次下线时间',
  `login_last_time_ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后一次登录ip',
  `user_regist_time` datetime(0) NOT NULL COMMENT '账号注册时间-非空',
  `user_status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '用户状态码-0:未注销;1:已注销;2:暂时被冻结;(默认为0)',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  `logout_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '用户注销标记：0-未注销，1-确认注销，2-取消注销',
  `app_code` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '1 STEAM课堂 2 北极星宠 3万象课堂',
  `credentials_id` int(0) NOT NULL,
  PRIMARY KEY (`user_id`) USING BTREE,
  INDEX `fk_credentials`(`credentials_id`) USING BTREE,
  CONSTRAINT `fk_credentials` FOREIGN KEY (`credentials_id`) REFERENCES `auth_credentials` (`credentials_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES (1, '猴恨崇慷瘩爸', 'http://39.108.67.16:9000/a-bucket/file_1692259795499_1692259795499.png', 0, 'Hello, I\'m John Doe.', 'California', 'Los Angeles', 'Downtown', -118.243683, 34.052235, NULL, NULL, '', '2023-08-17 08:09:39', 0, '2023-09-05 13:17:37', 0, '2', 20);
INSERT INTO `user_info` VALUES (11, '秒竞淀排蝉潮', 'http://39.108.67.16:9000/a-bucket/file_1694766879779_1694766879779.png', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2023-09-15 16:34:40', 0, NULL, 0, '1', 19);
INSERT INTO `user_info` VALUES (19, '簧恨刹绕谁梳', '/bcp/userinfo/50e7e1b31d1d33188dc0717ba1956923.png', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2024-01-29 18:33:20', 0, NULL, 0, '2', 28);

SET FOREIGN_KEY_CHECKS = 1;
