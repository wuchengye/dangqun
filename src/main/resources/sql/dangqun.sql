/*
 Navicat MySQL Data Transfer

 Source Server         : 本机
 Source Server Type    : MySQL
 Source Server Version : 50718
 Source Host           : localhost:3306
 Source Schema         : dangqun

 Target Server Type    : MySQL
 Target Server Version : 50718
 File Encoding         : 65001

 Date: 22/01/2021 17:12:05
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for auth_table
-- ----------------------------
DROP TABLE IF EXISTS `auth_table`;
CREATE TABLE `auth_table`  (
  `auth_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '权限id，自增',
  `auth_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限名',
  `auth_level` int(11) NULL DEFAULT NULL COMMENT '权限等级：0管理，1普通，2二级用户',
  `auth_branch` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '二级用户权限支持查看的支部',
  `auth_branch_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '二级用户权限支持查看的支部内部路径',
  `auth_default` int(11) NULL DEFAULT NULL COMMENT '权限是否默认，不允许修改删除',
  PRIMARY KEY (`auth_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of auth_table
-- ----------------------------
INSERT INTO `auth_table` VALUES (1, '管理员', 0, NULL, NULL, 1);
INSERT INTO `auth_table` VALUES (2, '普通用户', 1, NULL, NULL, 1);

-- ----------------------------
-- Table structure for branch_table
-- ----------------------------
DROP TABLE IF EXISTS `branch_table`;
CREATE TABLE `branch_table`  (
  `branch_id` int(11) NOT NULL AUTO_INCREMENT,
  `branch_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `branch_root_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `branch_creator` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`branch_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for file_table
-- ----------------------------
DROP TABLE IF EXISTS `file_table`;
CREATE TABLE `file_table`  (
  `file_id` int(11) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `file_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `file_track` int(11) NULL DEFAULT NULL,
  `file_branch` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`file_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for inner_track_table
-- ----------------------------
DROP TABLE IF EXISTS `inner_track_table`;
CREATE TABLE `inner_track_table`  (
  `track_id` int(11) NOT NULL AUTO_INCREMENT,
  `track_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `track_branch` int(11) NULL DEFAULT NULL,
  `track_inner_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `trach_full_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `trach_left_value` int(11) NULL DEFAULT NULL,
  `track_right_value` int(11) NULL DEFAULT NULL,
  `track_parent_id` int(11) NULL DEFAULT NULL,
  `track_status` int(11) NULL DEFAULT NULL COMMENT '路径状态：0存文件1存文件夹2都可以',
  PRIMARY KEY (`track_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_table
-- ----------------------------
DROP TABLE IF EXISTS `user_table`;
CREATE TABLE `user_table`  (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id，自增',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账户',
  `user_pwd` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `user_auth` int(11) NULL DEFAULT NULL COMMENT '权限，权限表的id',
  `user_branch` int(11) NULL DEFAULT NULL COMMENT '支部，支部表的id',
  `user_creator` int(11) NULL DEFAULT NULL COMMENT '创建者',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_table
-- ----------------------------
INSERT INTO `user_table` VALUES (1, 'admin', 'admin', 1, NULL, NULL);
INSERT INTO `user_table` VALUES (2, 'user1', 'user1', 2, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
