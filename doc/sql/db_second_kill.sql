/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50718
 Source Host           : localhost:3306
 Source Schema         : db_second_kill

 Target Server Type    : MySQL
 Target Server Version : 50718
 File Encoding         : 65001

 Date: 26/12/2019 12:50:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for item
-- ----------------------------
DROP TABLE IF EXISTS `item`;
CREATE TABLE `item`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名',
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品编号',
  `kill_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '秒杀价格',
  `original_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品原价',
  `img_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品图片',
  `stock` bigint(20) NULL DEFAULT NULL COMMENT '库存',
  `purchase_time` date NULL DEFAULT NULL COMMENT '采购时间',
  `is_active` int(11) NULL DEFAULT 1 COMMENT '是否有效（1=是；0=否）',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of item
-- ----------------------------
INSERT INTO `item` VALUES (6, '宝宝五彩舒服棉袜', 'item10010', 19.90, 30.00, 'http://localhost:8010/img/s_img2.jpg', 1000, '2019-05-18', 1, '2019-05-18 21:11:23', NULL);
INSERT INTO `item` VALUES (7, '宝宝五彩舒服棉制服', 'item10011', 66.66, 99.99, 'http://localhost:8010/img/s_img3.jpg', 2000, '2019-05-18', 1, '2019-05-18 21:11:23', NULL);
INSERT INTO `item` VALUES (8, '可爱宝宝茶杯套装', 'item10012', 28.80, 38.80, 'http://localhost:8010/img/s_img4.jpg', 2000, '2019-05-18', 1, '2019-05-18 21:11:23', NULL);
INSERT INTO `item` VALUES (9, '宝宝夜晚睡衣舒服', 'item10013', 58.80, 78.00, 'http://localhost:8010/img/s_img5.jpg', 2000, '2019-12-04', 1, '2019-12-04 10:59:27', NULL);

-- ----------------------------
-- Table structure for item_kill
-- ----------------------------
DROP TABLE IF EXISTS `item_kill`;
CREATE TABLE `item_kill`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `item_id` int(11) NULL DEFAULT NULL COMMENT '商品id',
  `total` int(11) NULL DEFAULT NULL COMMENT '可被秒杀的总数',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '秒杀开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '秒杀结束时间',
  `is_active` tinyint(11) NULL DEFAULT 1 COMMENT '是否有效（1=是；0=否）',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建的时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '待秒杀商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of item_kill
-- ----------------------------
INSERT INTO `item_kill` VALUES (1, 6, 9, '2019-12-06 11:59:19', '2019-12-06 12:59:11', 1, '2019-05-20 21:59:41');
INSERT INTO `item_kill` VALUES (2, 7, 0, '2019-12-06 11:59:19', '2019-12-07 21:59:11', 1, '2019-05-20 21:59:41');
INSERT INTO `item_kill` VALUES (3, 8, 100, '2019-12-06 11:58:26', '2019-12-31 21:59:07', 1, '2019-05-20 21:59:41');
INSERT INTO `item_kill` VALUES (4, 9, 85, '2019-12-06 12:39:14', '2019-12-31 12:39:25', 1, '2019-12-06 12:39:35');

-- ----------------------------
-- Table structure for item_kill_success
-- ----------------------------
DROP TABLE IF EXISTS `item_kill_success`;
CREATE TABLE `item_kill_success`  (
  `code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '秒杀成功生成的订单编号',
  `item_id` int(11) NULL DEFAULT NULL COMMENT '商品id',
  `kill_id` int(11) NULL DEFAULT NULL COMMENT '秒杀id',
  `user_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `status` tinyint(4) NULL DEFAULT -1 COMMENT '秒杀结果: -1无效  0成功(未付款)  1已付款  2已取消',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '秒杀成功订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of item_kill_success
-- ----------------------------
INSERT INTO `item_kill_success` VALUES ('406975276627800064', 8, 3, '1', -1, '2019-12-24 22:15:33');

-- ----------------------------
-- Table structure for random_code
-- ----------------------------
DROP TABLE IF EXISTS `random_code`;
CREATE TABLE `random_code`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_code`(`code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `phone` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮箱',
  `is_active` tinyint(11) NULL DEFAULT 1 COMMENT '是否有效(1=是；0=否)',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_user_name`(`user_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', 'd895dcd3445ef160182cea75f26f06d3', '15627280601', 'jacklinsir@sina.com', 1, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
