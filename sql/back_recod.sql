/*
 Navicat Premium Data Transfer

 Source Server         : localMySQL
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : localhost:3306
 Source Schema         : sharelock

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 04/12/2019 19:14:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for back_recod
-- ----------------------------
DROP TABLE IF EXISTS `back_recod`;
CREATE TABLE `back_recod`  (
  `id` int(9) NOT NULL AUTO_INCREMENT COMMENT '记录id',
  `user_id` int(9) NOT NULL COMMENT '用户id',
  `back_id` varchar(64) NOT NULL COMMENT '单车id',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `price` int(5) NULL DEFAULT NULL COMMENT '单价，单位是角',
  `t_price` int(9) NULL DEFAULT NULL COMMENT '总计，单位是角',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单车状态，0-正在使用，1-已关锁',
  `distance` int(9) NULL DEFAULT NULL COMMENT '行驶距离，单位米',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除标志',
  `remark` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '行车记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of back_recod
-- ----------------------------
INSERT INTO `back_recod` VALUES (1, 1, '1234567', '2019-12-04 19:00:02', '2019-12-04 19:10:15', 100, 100, '1', 500, '2019-12-04 19:00:02', 'admin', '2019-12-04 19:10:15', 'admin', '0', NULL);

SET FOREIGN_KEY_CHECKS = 1;
