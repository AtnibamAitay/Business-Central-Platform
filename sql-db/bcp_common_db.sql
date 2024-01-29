/*
 Navicat Premium Data Transfer

 Source Server         : 南社生产数据库
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : 39.108.67.16:3306
 Source Schema         : bcp_common_db

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 30/01/2024 00:00:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for file_info
-- ----------------------------
DROP TABLE IF EXISTS `file_info`;
CREATE TABLE `file_info`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件ID（MD5值）',
  `file_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件名称',
  `bucket` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '存储目录',
  `file_path` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '存储路径',
  `url` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件访问地址',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '上传人',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '上传时间',
  `change_date` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `status` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '状态,1:正常，0:禁用',
  `file_size` bigint(0) NULL DEFAULT NULL COMMENT '文件大小',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '媒资信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of file_info
-- ----------------------------
INSERT INTO `file_info` VALUES ('1e9f9df1a03f819e5f76fbb63fce8391', '01c451628b19e20002c3290feaa309.jpg', 'bcp', 'user/avatar/pic/25/1e9f9df1a03f819e5f76fbb63fce8391.jpg', '/bcp/user/avatar/pic/25/1e9f9df1a03f819e5f76fbb63fce8391.jpg', 25, '2024-01-28 16:06:51', NULL, '1', 1246653);
INSERT INTO `file_info` VALUES ('50e7e1b31d1d33188dc0717ba1956923', 'file_1706418759730.png', 'bcp', 'userinfo/50e7e1b31d1d33188dc0717ba1956923.png', '/bcp/userinfo/50e7e1b31d1d33188dc0717ba1956923.png', NULL, '2024-01-28 13:12:40', NULL, '1', 58362);

-- ----------------------------
-- Table structure for local_message_record
-- ----------------------------
DROP TABLE IF EXISTS `local_message_record`;
CREATE TABLE `local_message_record`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `service` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务名称 eg:订单服务',
  `business` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务名称 eg:创建订单',
  `model` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息模式：SYNC，ASYNC，ONEWAY',
  `topic` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'topic',
  `tags` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'tag',
  `msg_id` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消息id',
  `msg_key` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息key',
  `body` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息体',
  `status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '发送状态  0:发送中  1:重试中  2:发送失败  3:发送成功',
  `max_retry_times` tinyint(0) NOT NULL DEFAULT 5 COMMENT '最大重试次数',
  `current_retry_times` tinyint(0) NULL DEFAULT 0 COMMENT '当前重试次数',
  `send_success_time` datetime(0) NULL DEFAULT NULL COMMENT '发送成功时间',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `scheduled_time` datetime(0) NOT NULL COMMENT '消息触发时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 36 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of local_message_record
-- ----------------------------
INSERT INTO `local_message_record` VALUES (1, '订单服务', '创建订单', 'SYNC', 'order_topic', 'order_tag', 'msg_id_1', 'msg_key_1', 'Order message body 1', 2, 5, 5, NULL, '2023-08-28 13:52:58', '2023-09-02 04:00:48', '2023-08-31 15:30:27');
INSERT INTO `local_message_record` VALUES (2, '支付服务', '创建支付', 'ASYNC', 'payment_topic', 'payment_tag', 'msg_id_2', 'msg_key_2', 'Payment message body 2', 2, 5, 5, NULL, '2023-08-28 13:52:58', '2023-09-02 04:00:48', '2023-08-31 15:30:27');
INSERT INTO `local_message_record` VALUES (3, '用户服务', '创建用户', 'SYNC', 'user_topic', 'user_tag', 'msg_id_3', 'msg_key_3', 'User message body 3', 0, 5, 0, NULL, '2023-08-28 13:52:58', '2023-09-02 04:00:48', '2023-08-31 15:30:27');
INSERT INTO `local_message_record` VALUES (4, '订单服务', '更新订单', 'SYNC', 'order_topic', 'order_tag', 'msg_id_4', 'msg_key_4', 'Order message body 4', 0, 5, 0, NULL, '2023-08-28 13:52:58', '2023-09-02 04:00:47', '2023-08-31 15:30:27');
INSERT INTO `local_message_record` VALUES (5, '通知服务', '发送通知', 'ASYNC', 'notification_topic', 'notification_tag', 'msg_id_5', 'msg_key_5', 'Notification message body 5', 0, 5, 0, NULL, '2023-08-28 13:52:58', '2023-09-02 04:00:48', '2023-08-31 15:30:27');
INSERT INTO `local_message_record` VALUES (6, '支付服务', '更新支付', 'SYNC', 'payment_topic', 'payment_tag', 'msg_id_6', 'msg_key_6', 'Payment message body 6', 0, 5, 0, NULL, '2023-08-28 13:52:58', '2023-09-02 04:00:48', '2023-08-31 15:30:27');
INSERT INTO `local_message_record` VALUES (7, '用户服务', '更新用户', 'SYNC', 'user_topic', 'user_tag', 'msg_id_7', 'msg_key_7', 'User message body 7', 0, 5, 0, NULL, '2023-08-28 13:52:58', '2023-09-02 04:00:48', '2023-08-31 15:30:27');
INSERT INTO `local_message_record` VALUES (8, '订单服务', '删除订单', 'SYNC', 'order_topic', 'order_tag', 'msg_id_8', 'msg_key_8', 'Order message body 8', 0, 5, 0, NULL, '2023-08-28 13:52:58', '2023-09-02 04:00:48', '2023-08-31 15:30:27');
INSERT INTO `local_message_record` VALUES (9, '通知服务', '删除通知', 'ASYNC', 'notification_topic', 'notification_tag', 'msg_id_9', 'msg_key_9', 'Notification message body 9', 0, 5, 0, NULL, '2023-08-28 13:52:58', '2023-09-02 04:00:48', '2023-08-31 15:30:27');
INSERT INTO `local_message_record` VALUES (10, '用户服务', '删除用户', 'SYNC', 'user_topic', 'user_tag', 'msg_id_10', 'msg_key_10', 'User message body 10', 0, 5, 0, NULL, '2023-08-28 13:52:58', '2023-09-02 04:00:48', '2023-08-31 15:30:27');
INSERT INTO `local_message_record` VALUES (11, '测试服务', '修改订单', 'SYNC', 'order', 'create', 'abc123', 'order_create_123', 'Order details', 3, 5, 0, '2023-08-26 18:30:00', '2023-08-26 16:00:00', '2023-09-02 04:00:48', '2023-08-31 15:30:27');
INSERT INTO `local_message_record` VALUES (25, 'user-service', 'delay-logout', 'ASYNC', 'delay_topic', 'logout_delay_tag', '7F0000014AB018B4AAC21772C4620000', '6919f0cc-df84-4e8a-b7e6-4bbabfeddfca', '\"1\"', 3, 5, 0, '2023-09-05 13:16:38', '2023-09-05 13:14:14', '2023-09-05 05:16:20', '2023-09-20 08:00:00');

SET FOREIGN_KEY_CHECKS = 1;
