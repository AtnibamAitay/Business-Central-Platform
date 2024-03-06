/*
 Navicat Premium Data Transfer

 Source Server         : bcp-db
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : localhost:3306
 Source Schema         : bcp_db

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 06/03/2024 21:19:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for app
-- ----------------------------
DROP TABLE IF EXISTS `app`;
CREATE TABLE `app`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '应用名',
  `status` tinyint(0) NOT NULL COMMENT '状态',
  `payment_call_back_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '支付结果的回调地址',
  `refund_call_back_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '退款结果的回调地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article`  (
  `article_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '文章Id',
  `title` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '文章内容',
  `create_time` date NOT NULL COMMENT '创建时间',
  `update_time` date NOT NULL COMMENT '修改时间',
  `deleted` tinyint(0) NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`article_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`
(
    `id`          int(0)                                                  NOT NULL AUTO_INCREMENT COMMENT '评论ID',
    `user_id`     int(0)                                                  NULL     DEFAULT NULL COMMENT '用户ID',
    `object_id`   int(0)                                                  NOT NULL COMMENT '对象ID（文章/视频/商品/父评论）',
    `object_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '对象类型（0代表文章评论、1代表视频评论、2代表商品评论、3代表评论的子评论）',
    `grade`       int(0)                                                  NULL     DEFAULT NULL COMMENT '评分',
    `content`     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '内容',
    `type`        tinyint(0)                                              NOT NULL DEFAULT 0 COMMENT '内容类型（0代表文本，1代表表情包，2代表图片）',
    `deleted`     tinyint(0)                                              NOT NULL DEFAULT 1 COMMENT '是否删除（0代表已删除，1代表正常）',
    `create_time` datetime(0)                                             NULL     DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for coupon_min_spend_thresholds
-- ----------------------------
DROP TABLE IF EXISTS `coupon_min_spend_thresholds`;
CREATE TABLE `coupon_min_spend_thresholds`
(
    `threshold_id`     int(0)         NOT NULL AUTO_INCREMENT COMMENT '满减券门槛ID',
    `coupon_id`        int(0)         NOT NULL COMMENT '优惠券ID',
    `min_order_amount` decimal(10, 2) NOT NULL COMMENT '最低订单金额',
    `discount_amount`  decimal(10, 2) NOT NULL COMMENT '优惠金额',
    PRIMARY KEY (`threshold_id`) USING BTREE,
    INDEX `coupon_id` (`coupon_id`) USING BTREE,
    CONSTRAINT `coupon_min_spend_thresholds_ibfk_1` FOREIGN KEY (`coupon_id`) REFERENCES `coupons` (`coupon_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '满减券门槛表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for coupons
-- ----------------------------
DROP TABLE IF EXISTS `coupons`;
CREATE TABLE `coupons`
(
    `coupon_id`      int(0)                                                        NOT NULL AUTO_INCREMENT COMMENT '优惠券ID',
    `coupon_name`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '优惠券名',
    `redeem_code`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '优惠券兑换码',
    `start_date`     datetime(0)                                                   NULL     DEFAULT NULL COMMENT '开始日期',
    `coupon_type`    tinyint(0)                                                    NOT NULL COMMENT '优惠券类型（1满减券、2直减券、3折扣券）',
    `issuer_id`      int(0)                                                        NOT NULL COMMENT '发券人ID',
    `issue_quantity` int(0)                                                        NOT NULL DEFAULT 0 COMMENT '发行量（0代表无限制）',
    `app_id`         int(0)                                                        NOT NULL COMMENT 'APPID',
    `status`         tinyint(0)                                                    NOT NULL DEFAULT 1 COMMENT '状态（0已删除、1正常）',
    PRIMARY KEY (`coupon_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '优惠券表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for file_info
-- ----------------------------
DROP TABLE IF EXISTS `file_info`;
CREATE TABLE `file_info`
(
    `id`          varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci   NOT NULL COMMENT '文件ID（MD5值）',
    `file_name`   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '文件名称',
    `bucket`      varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL DEFAULT NULL COMMENT '存储目录',
    `file_path`   varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL DEFAULT NULL COMMENT '存储路径',
    `url`         varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件访问地址',
    `user_id`     int(0)                                                   NULL DEFAULT NULL COMMENT '上传人',
    `create_date` datetime(0)                                              NULL DEFAULT NULL COMMENT '上传时间',
    `change_date` datetime(0)                                              NULL DEFAULT NULL COMMENT '修改时间',
    `status`      varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci   NULL DEFAULT '1' COMMENT '状态,1:正常，0:禁用',
    `file_size`   bigint(0)                                                NULL DEFAULT NULL COMMENT '文件大小',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '媒资信息'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for friend_relation
-- ----------------------------
DROP TABLE IF EXISTS `friend_relation`;
CREATE TABLE `friend_relation`
(
    `relation_id`  int(0)                                                        NOT NULL AUTO_INCREMENT COMMENT '关系id',
    `friend_id`    int(0)                                                        NOT NULL COMMENT '好友id',
    `own_id`       int(0)                                                        NOT NULL COMMENT '自己的id',
    `alias`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '备注用户名',
    `relationship` tinyint(0)                                                    NOT NULL DEFAULT 1 COMMENT '关系（0代表黑名单好友、1代表正常好友）',
    PRIMARY KEY (`relation_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '好友表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for friend_request
-- ----------------------------
DROP TABLE IF EXISTS `friend_request`;
CREATE TABLE `friend_request`
(
    `request_id`     int(0)                                                        NOT NULL AUTO_INCREMENT COMMENT '请求ID',
    `user_id_adding` int(0)                                                        NOT NULL COMMENT '请求增添的用户ID',
    `user_id_added`  int(0)                                                        NOT NULL COMMENT '请求被增添的用户ID',
    `message`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '附言',
    `status_code`    tinyint(0)                                                    NOT NULL DEFAULT 0 COMMENT '添加状态码（0代表未处理、1代表已拒绝）',
    PRIMARY KEY (`request_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '增添好友请求表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for local_message_record
-- ----------------------------
DROP TABLE IF EXISTS `local_message_record`;
CREATE TABLE `local_message_record`
(
    `id`                bigint(0)                                              NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `service`           varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务名称 eg:订单服务',
    `business`          varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务名称 eg:创建订单',
    `model`             varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息模式：SYNC，ASYNC，ONEWAY',
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
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `title` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订单标题',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '平台的订单编号',
  `user_id` bigint(0) NULL DEFAULT NULL COMMENT '用户id',
  `product_id` bigint(0) NULL DEFAULT NULL COMMENT '支付产品id',
  `total_fee` int(0) NULL DEFAULT NULL COMMENT '订单金额(分)',
  `payment_data` varchar(1500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '微信支付的订单二维码链接或者支付宝支付的HTML表单',
  `order_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订单状态',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `payment_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支付方式',
  `app_id` int(0) NULL DEFAULT NULL COMMENT 'AppID',
  `payment_call_back_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支付结果的回调地址',
  `refund_call_back_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退款结果的回调地址',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_orderId`(`id`) USING BTREE,
  INDEX `idx_userId`(`user_id`) USING BTREE,
  INDEX `idx_productId`(`product_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for payment_info
-- ----------------------------
DROP TABLE IF EXISTS `payment_info`;
CREATE TABLE `payment_info`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '支付记录id',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '平台的订单编号',
  `transaction_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支付系统交易编号',
  `payment_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支付类型',
  `trade_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '交易类型',
  `trade_state` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '交易状态',
  `payer_total` int(0) NULL DEFAULT NULL COMMENT '支付金额(分)',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '通知参数',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for refund_info
-- ----------------------------
DROP TABLE IF EXISTS `refund_info`;
CREATE TABLE `refund_info`  (
                                `id`             bigint(0) UNSIGNED                                           NOT NULL AUTO_INCREMENT COMMENT '退款单id',
                                `order_no`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '平台的订单编号',
                                `refund_no`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '平台的退款单编号',
                                `refund_id`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支付系统退款单号',
                                `total_fee`      int(0)                                                       NULL DEFAULT NULL COMMENT '原订单金额(分)',
                                `refund`         int(0)                                                       NULL DEFAULT NULL COMMENT '退款金额(分)',
                                `reason`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退款原因',
                                `refund_status`  varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退款状态',
                                `content_return` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci        NULL COMMENT '申请退款返回参数',
                                `content_notify` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci        NULL COMMENT '退款结果通知参数',
                                `create_time`    datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
                                `update_time`    datetime(0)                                                  NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for shopping_cart
-- ----------------------------
DROP TABLE IF EXISTS `shopping_cart`;
CREATE TABLE `shopping_cart`
(
    `cart_id`           int(0)         NOT NULL AUTO_INCREMENT COMMENT '购物车主键',
    `user_id`           int(0)         NOT NULL COMMENT '用户id',
    `spu_id`            int(0)         NOT NULL COMMENT 'SPU ID',
    `sku_id`            int(0)         NOT NULL COMMENT 'SKU ID',
    `quantity`          int(0)         NOT NULL DEFAULT 1 COMMENT '商品数量',
    `price_at_addition` decimal(10, 2) NOT NULL COMMENT '加入购物车时的价格',
    PRIMARY KEY (`cart_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for spec
-- ----------------------------
DROP TABLE IF EXISTS `spec`;
CREATE TABLE `spec`
(
    `spec_id` int(0)         NOT NULL AUTO_INCREMENT COMMENT '规格id',
    `spu_id`  int(0)         NOT NULL COMMENT '商品id',
    `specn`   json           NOT NULL COMMENT '规格',
    `stock`   int(0)         NOT NULL DEFAULT 0 COMMENT '库存',
    `price`   decimal(10, 2) NOT NULL COMMENT '价格',
    PRIMARY KEY (`spec_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '规格表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for spec_name
-- ----------------------------
DROP TABLE IF EXISTS `spec_name`;
CREATE TABLE `spec_name`
(
    `spec_name_id`  int(0)                                                        NOT NULL AUTO_INCREMENT COMMENT '规格名id',
    `spu_id`        int(0)                                                        NOT NULL COMMENT '商品id',
    `spec_name`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '规格名',
    `display_order` int(0)                                                        NOT NULL DEFAULT 0 COMMENT '显示顺序',
    PRIMARY KEY (`spec_name_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '规格名表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for spec_value
-- ----------------------------
DROP TABLE IF EXISTS `spec_value`;
CREATE TABLE `spec_value`
(
    `spec_value_id`        int(0)                                                        NOT NULL AUTO_INCREMENT COMMENT '规格值id',
    `spec_name_id`         int(0)                                                        NOT NULL COMMENT '规格名id',
    `spec_value_name`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '规格值',
    `spec_value_image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '规格值图',
    `display_order`        int(0)                                                        NOT NULL DEFAULT 0 COMMENT '显示顺序',
    PRIMARY KEY (`spec_value_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '规格值表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for spu
-- ----------------------------
DROP TABLE IF EXISTS `spu`;
CREATE TABLE `spu`
(
    `spu_id`          int(0)                                                        NOT NULL AUTO_INCREMENT COMMENT '商品id',
    `merchant_id`     int(0)                                                        NOT NULL COMMENT '商户id',
    `name`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名',
    `sales_volume`    int(0)                                                        NOT NULL DEFAULT 0 COMMENT '累计销量',
    `shipment_origin` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '发货地',
    `shipping_fee`    decimal(10, 2)                                                NOT NULL COMMENT '运费',
    `creation_time`   datetime(0)                                                   NOT NULL COMMENT '商品创建时间',
    `is_listed`       tinyint(0)                                                    NOT NULL DEFAULT 1 COMMENT '是否上架（0表示下架、1表示）',
    `status_code`     tinyint(0)                                                    NOT NULL DEFAULT 1 COMMENT '状态码（0代表已删除，1代表正常）',
    PRIMARY KEY (`spu_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for spu_cover
-- ----------------------------
DROP TABLE IF EXISTS `spu_cover`;
CREATE TABLE `spu_cover`
(
    `cover_id`      int(0)                                                        NOT NULL AUTO_INCREMENT COMMENT 'spu封面id',
    `spu_id`        int(0)                                                        NOT NULL COMMENT '商品id',
    `cover_url`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '封面url',
    `display_order` int(0)                                                        NOT NULL DEFAULT 0 COMMENT '显示顺序',
    PRIMARY KEY (`cover_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = 'SPU封面表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for spu_detail
-- ----------------------------
DROP TABLE IF EXISTS `spu_detail`;
CREATE TABLE `spu_detail`
(
    `detail_id`     int(0)                                                        NOT NULL AUTO_INCREMENT COMMENT 'spu详细介绍id',
    `spu_id`        int(0)                                                        NOT NULL COMMENT '商品id',
    `detail_url`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'spu详细图文url',
    `display_order` int(0)                                                        NOT NULL DEFAULT 0 COMMENT '显示顺序',
    PRIMARY KEY (`detail_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = 'SPU详细表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_coupons
-- ----------------------------
DROP TABLE IF EXISTS `user_coupons`;
CREATE TABLE `user_coupons`
(
    `id`           int(0)      NOT NULL AUTO_INCREMENT COMMENT '主键',
    `coupon_id`    int(0)      NOT NULL COMMENT '优惠券id',
    `user_id`      int(0)      NOT NULL COMMENT '用户id',
    `acquire_time` datetime(0) NOT NULL COMMENT '获得时间',
    `expire_date`  datetime(0) NULL     DEFAULT NULL COMMENT '失效日期',
    `status`       tinyint(0)  NOT NULL DEFAULT 1 COMMENT '使用情况（0代表已使用、1代表未使用、2代表已过期）',
    `use_time`     datetime(0) NULL     DEFAULT NULL COMMENT '使用时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户的优惠券表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`
(
    `user_id`              int(0)                                                   NOT NULL AUTO_INCREMENT COMMENT '用户ID-主键',
    `user_name`            varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci   NOT NULL DEFAULT '' COMMENT '用户名-默认值(应该是随机生成)',
    `user_avatar`          varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '头像-默认（固定默认头像）',
    `user_role`            tinyint(0)                                               NOT NULL DEFAULT 0 COMMENT '用户角色-0:平台管理员;1:机构管理员;2:老师;3:学生;(默认为0)',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
