/*
Navicat MySQL Data Transfer

Source Server         : MySQL_localhost
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : blade

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2016-12-01 12:20:20
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_yw_tzgg
-- ----------------------------
DROP TABLE IF EXISTS `tb_yw_tzgg`;
CREATE TABLE `tb_yw_tzgg` (
`F_IT_XL`  int(11) NOT NULL AUTO_INCREMENT COMMENT '序列' ,
`F_VC_BT`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题' ,
`F_IT_LX`  int(11) NULL DEFAULT NULL COMMENT '类型' ,
`F_TX_NR`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '内容' ,
`F_DT_FBSJ`  datetime NULL DEFAULT NULL COMMENT '发布时间' ,
`F_DT_CJSJ`  datetime NULL DEFAULT NULL COMMENT '创建时间' ,
`F_IT_CJR`  int(11) NULL DEFAULT NULL COMMENT '创建人' ,
`F_IT_TP`  int(11) NULL DEFAULT NULL COMMENT '图片' ,
`VERSION`  int(11) NULL DEFAULT NULL ,
PRIMARY KEY (`F_IT_XL`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=6

;

-- ----------------------------
-- Records of tb_yw_tzgg
-- ----------------------------
BEGIN;
INSERT INTO `tb_yw_tzgg` VALUES ('3', '测试233', '2', '123123', '2016-11-21 00:00:00', null, null, null, '5'), ('4', 'test', '10', '1', '2016-11-21 00:00:00', null, null, null, '1'), ('5', 'test233', '10', '2323', '2016-11-22 00:00:00', null, null, null, null);
COMMIT;

-- ----------------------------
-- Table structure for tfw_attach
-- ----------------------------
DROP TABLE IF EXISTS `tfw_attach`;
CREATE TABLE `tfw_attach` (
`ID`  int(11) NOT NULL AUTO_INCREMENT ,
`CODE`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`NAME`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`URL`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
`STATUS`  int(11) NULL DEFAULT NULL ,
`CREATER`  int(11) NULL DEFAULT NULL ,
`CREATETIME`  datetime NULL DEFAULT NULL ,
PRIMARY KEY (`ID`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=323

;

-- ----------------------------
-- Records of tfw_attach
-- ----------------------------
BEGIN;
INSERT INTO `tfw_attach` VALUES ('290', null, 'Desert.jpg', '/upload/20160222/20160222085146_506.jpg', '1', '1', '2016-02-22 08:51:46'), ('291', null, 'Chrysanthemum.jpg', '/upload\\20160319\\1458372187438.jpg', '1', '1', '2016-03-19 15:23:07'), ('292', null, '11.png', '/upload\\20160415\\1460705916912.png', '1', '1', '2016-04-15 15:38:36'), ('293', null, '11.png', '/upload\\20160415\\1460706087306.png', '1', '1', '2016-04-15 15:41:27'), ('294', null, '14ce36d3d539b600ac7ff8a1ea50352ac65cb777.jpg', '/upload\\20160612\\1465711014299.jpg', '1', '1', '2016-06-12 13:56:54'), ('295', null, '{UGU13_}_[5[BG0G5Y]TFKX.jpg', '/upload\\20161102\\1478078033153.jpg', '1', '1', '2016-11-02 17:13:53'), ('296', null, '{UGU13_}_[5[BG0G5Y]TFKX.jpg', '/upload\\20161102\\1478078110207.jpg', '1', '1', '2016-11-02 17:15:10'), ('297', null, '{UGU13_}_[5[BG0G5Y]TFKX.jpg', '/upload\\20161102\\1478078175711.jpg', '1', '1', '2016-11-02 17:16:15'), ('298', null, '{UGU13_}_[5[BG0G5Y]TFKX.jpg', '/upload\\20161103\\1478133453564.jpg', '1', '1', '2016-11-03 08:37:33'), ('299', null, '{UGU13_}_[5[BG0G5Y]TFKX.jpg', '/upload\\20161103\\1478134282895.jpg', '1', '1', '2016-11-03 08:51:22'), ('300', null, '{UGU13_}_[5[BG0G5Y]TFKX.jpg', '/upload\\20161103\\1478134309972.jpg', '1', '1', '2016-11-03 08:51:49'), ('301', null, '{UGU13_}_[5[BG0G5Y]TFKX.jpg', '/upload\\20161103\\1478134742768.jpg', '1', '1', '2016-11-03 08:59:02'), ('302', null, '9NNL%%_RO2P4M%SFUN%DL[J.jpg', '/upload\\20161103\\1478134822691.jpg', '1', '1', '2016-11-03 09:00:22'), ('303', null, '{UGU13_}_[5[BG0G5Y]TFKX.jpg', '/upload\\20161103\\1478134846052.jpg', '1', '1', '2016-11-03 09:00:46'), ('304', null, '9NNL%%_RO2P4M%SFUN%DL[J.jpg', '/upload\\20161103\\1478134855111.jpg', '1', '1', '2016-11-03 09:00:55'), ('305', null, '9NNL%%_RO2P4M%SFUN%DL[J.jpg', '/upload\\20161103\\1478134872659.jpg', '1', '1', '2016-11-03 09:01:12'), ('306', null, '{UGU13_}_[5[BG0G5Y]TFKX.jpg', '/upload\\20161103\\1478134878092.jpg', '1', '1', '2016-11-03 09:01:18'), ('307', null, '14ce36d3d539b600ac7ff8a1ea50352ac65cb777.jpg', '/upload\\20161103\\1478134886188.jpg', '1', '1', '2016-11-03 09:01:26'), ('308', null, 'b21bb051f8198618e7dfe36848ed2e738ad4e646.gif', null, '1', '1', '2016-11-03 09:11:42'), ('309', null, 'b21bb051f8198618e7dfe36848ed2e738ad4e646.gif', '/upload\\20161103\\1478135567064.gif', '1', '1', '2016-11-03 09:13:41'), ('310', null, 'Chrysanthemum.jpg', '/upload\\20161103\\1478135757792.jpg', '1', '1', '2016-11-03 09:16:21'), ('311', null, 'Chrysanthemum.jpg', '/upload\\20161103\\1478135882738.jpg', '1', '1', '2016-11-03 09:18:02'), ('312', null, 'b21bb051f8198618e7dfe36848ed2e738ad4e646.gif', '/upload\\20161103\\1478135892950.gif', '1', '1', '2016-11-03 09:18:12'), ('313', null, 'b21bb051f8198618e7dfe36848ed2e738ad4e646.gif', '/upload\\20161103\\1478138730552.gif', '1', '1', '2016-11-03 10:05:44'), ('314', null, 'Jellyfish.jpg', '/upload\\20161103\\1478138871357.jpg', '1', '1', '2016-11-03 10:07:51'), ('315', null, 'e1fe9925bc315c60e68798f98eb1cb1349547792.jpg', '/upload\\20161103\\1478139210387.jpg', '1', '1', '2016-11-03 10:13:30'), ('316', null, 'b21bb051f8198618e7dfe36848ed2e738ad4e646.gif', '/upload\\20161103\\1478139282831.gif', '1', '1', '2016-11-03 10:14:42'), ('317', null, 'b21bb051f8198618e7dfe36848ed2e738ad4e646.gif', '/upload\\20161103\\1478139813380.gif', '1', '1', '2016-11-03 10:23:33'), ('318', null, 'b21bb051f8198618e7dfe36848ed2e738ad4e646.gif', '/upload\\20161103\\1478139971214.gif', '1', '1', '2016-11-03 10:26:11'), ('319', null, 'b21bb051f8198618e7dfe36848ed2e738ad4e646.gif', '/upload\\20161104\\\\image\\1478220893183.gif', '1', '1', '2016-11-04 08:54:53'), ('320', null, 'Jellyfish.jpg', '/upload\\image\\20161104\\1478221195693.jpg', '1', '1', '2016-11-04 08:59:55'), ('321', null, 'b21bb051f8198618e7dfe36848ed2e738ad4e646.gif', '/upload\\image\\20161104\\1478221205733.gif', '1', '1', '2016-11-04 09:00:05'), ('322', null, 'b21bb051f8198618e7dfe36848ed2e738ad4e646.gif', '/upload\\image\\20161107\\1478500038709.gif', '1', '1', '2016-11-07 14:27:18');
COMMIT;

-- ----------------------------
-- Table structure for tfw_dept
-- ----------------------------
DROP TABLE IF EXISTS `tfw_dept`;
CREATE TABLE `tfw_dept` (
`ID`  int(11) NOT NULL AUTO_INCREMENT ,
`NUM`  int(11) NULL DEFAULT NULL ,
`PID`  int(11) NULL DEFAULT NULL ,
`SIMPLENAME`  varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`FULLNAME`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`TIPS`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`VERSION`  int(11) NULL DEFAULT NULL ,
PRIMARY KEY (`ID`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=14

;

-- ----------------------------
-- Records of tfw_dept
-- ----------------------------
BEGIN;
INSERT INTO `tfw_dept` VALUES ('1', '0', '0', 'Tonbu', 'Tonbu', null, '1'), ('2', '1', '1', '江苏同步', '江苏同步信息技术有限公司', null, '1'), ('3', '1', '2', '技服部', '技术服务部', null, '2'), ('4', '2', '2', '客服部', '客户服务部', null, '1'), ('5', '3', '2', '销售部', '销售部', null, '1'), ('6', '4', '2', '综合管理部', '综合管理部', null, '1'), ('7', '2', '1', '常州同步', '常州同步软件技术有限公司', null, '1'), ('8', '1', '7', '产品部', '产品部', null, '1'), ('9', '2', '7', '研发部', '研发部', null, '1'), ('10', '3', '7', '项目部', '项目部', null, '1'), ('11', '4', '7', '运维部', '运维部', null, '1'), ('12', '5', '7', '销售部', '销售部', null, '1'), ('13', '6', '7', '行政部', '行政部', null, '1');
COMMIT;

-- ----------------------------
-- Table structure for tfw_dict
-- ----------------------------
DROP TABLE IF EXISTS `tfw_dict`;
CREATE TABLE `tfw_dict` (
`ID`  int(11) NOT NULL AUTO_INCREMENT ,
`CODE`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`NUM`  int(11) NULL DEFAULT NULL ,
`PID`  int(11) NULL DEFAULT NULL ,
`NAME`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`TIPS`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`VERSION`  int(11) NULL DEFAULT NULL ,
PRIMARY KEY (`ID`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=46

;

-- ----------------------------
-- Records of tfw_dict
-- ----------------------------
BEGIN;
INSERT INTO `tfw_dict` VALUES ('1', '101', '0', '0', '性别', null, '0'), ('2', '101', '1', '1', '男', null, '1'), ('3', '101', '2', '1', '女', null, '5'), ('5', '901', '0', '0', '账号状态', null, '0'), ('6', '901', '1', '5', '启用', null, '0'), ('7', '901', '2', '5', '冻结', null, '0'), ('8', '901', '3', '5', '待审核', null, '2'), ('9', '901', '4', '5', '审核拒绝', null, '0'), ('10', '901', '5', '5', '已删除', null, '3'), ('11', '902', '0', '0', '状态', null, '0'), ('12', '902', '1', '11', '启用', null, '0'), ('13', '902', '2', '11', '禁用', null, '0'), ('14', '102', '0', '0', '公告类型', null, '0'), ('15', '102', '10', '14', '通知公告', null, '0'), ('16', '102', '9', '14', '发布计划', null, '0'), ('17', '903', '0', '0', '审核状态', null, '0'), ('18', '903', '1', '17', '待审核', null, '0'), ('19', '903', '2', '17', '审核拒绝', null, '0'), ('20', '903', '3', '17', '审核通过', null, '0'), ('41', '102', '6', '16', '测试', null, '0'), ('44', '102', '1', '14', '发布测试', null, '0'), ('45', '102', '2', '16', '测试222', null, '2');
COMMIT;

-- ----------------------------
-- Table structure for tfw_generate
-- ----------------------------
DROP TABLE IF EXISTS `tfw_generate`;
CREATE TABLE `tfw_generate` (
`ID`  int(11) NOT NULL AUTO_INCREMENT ,
`NAME`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`REALPATH`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`PACKAGENAME`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`MODELNAME`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`TABLENAME`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`PKNAME`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`TIPS`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
PRIMARY KEY (`ID`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=2

;

-- ----------------------------
-- Records of tfw_generate
-- ----------------------------
BEGIN;
INSERT INTO `tfw_generate` VALUES ('1', '测试', 'E:\\Workspaces\\blade\\SpringBlade', 'com.smallchill.test', 'Menu', 'tfw_menu', 'ID', null);
COMMIT;

-- ----------------------------
-- Table structure for tfw_login_log
-- ----------------------------
DROP TABLE IF EXISTS `tfw_login_log`;
CREATE TABLE `tfw_login_log` (
`ID`  int(65) NOT NULL AUTO_INCREMENT ,
`LOGNAME`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`USERID`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`CLASSNAME`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`METHOD`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
`CREATETIME`  datetime NULL DEFAULT NULL ,
`SUCCEED`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`MESSAGE`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
PRIMARY KEY (`ID`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=1

;

-- ----------------------------
-- Records of tfw_login_log
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for tfw_menu
-- ----------------------------
DROP TABLE IF EXISTS `tfw_menu`;
CREATE TABLE `tfw_menu` (
`ID`  int(65) NOT NULL AUTO_INCREMENT ,
`CODE`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单编号' ,
`PCODE`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`ALIAS`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`NAME`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`ICON`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`URL`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`NUM`  int(65) NULL DEFAULT NULL ,
`LEVELS`  int(65) NULL DEFAULT NULL ,
`SOURCE`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
`PATH`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`TIPS`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`STATUS`  int(65) NULL DEFAULT NULL ,
`ISOPEN`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`ISTEMPLATE`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`VERSION`  int(11) NULL DEFAULT NULL ,
PRIMARY KEY (`ID`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=105

;

-- ----------------------------
-- Records of tfw_menu
-- ----------------------------
BEGIN;
INSERT INTO `tfw_menu` VALUES ('1', 'system', '0', null, '系统管理', 'fa-cog', null, '9', '1', null, null, null, '1', '1', '0', '3'), ('2', 'role', 'system', null, '角色管理', 'fa-key', '/role/', '2', '2', null, null, null, '1', '0', null, '1'), ('3', 'role_add', 'role', 'addex', '角色新增', 'btn btn-xs btn-white | fa fa-floppy-o bigger-120', '/role/add', '1', '3', null, 'role_add.html', '800*340', '1', '0', null, '3'), ('4', 'role_edit', 'role', 'edit', '修改', 'btn btn-xs btn-white | fa fa-pencil  bigger-120', '/role/edit', '2', '3', null, 'role_edit.html', '800*340', '1', '0', '0', '1'), ('5', 'role_remove', 'role', 'remove', '删除', 'btn btn-xs btn-white | fa fa-times  bigger-120', '/role/remove', '3', '3', null, null, null, '1', '0', null, '1'), ('6', 'role_view', 'role', 'view', '查看', 'btn btn-xs btn-white | fa fa-eye bigger-120', '/role/view', '4', '3', null, 'role_view.html', '800*340', '1', null, null, '1'), ('7', 'role_authority', 'role', 'authority', '权限配置', 'btn btn-xs btn-white | fa fa-wrench  bigger-120', '/role/authority', '5', '3', null, 'role_authority.html', '350*500', '1', '0', null, '2'), ('8', 'user', 'system', null, '用户管理', 'fa-user', '/user/', '1', '2', null, null, null, '1', null, null, '0'), ('9', 'user_add', 'user', 'add', '新增', 'btn btn-xs btn-white | fa fa-floppy-o bigger-120', '/user/add', '1', '3', null, 'user_add.html', '800*430', '1', null, null, '0'), ('10', 'user_edit', 'user', 'edit', '修改', 'btn btn-xs btn-white | fa fa-pencil  bigger-120', '/user/edit', '2', '3', null, 'user_edit.html', '800*430', '1', null, null, '0'), ('11', 'user_del', 'user', 'remove', '删除', 'btn btn-xs btn-white | fa fa fa-times bigger-120', '/user/del', '3', '3', null, null, null, '1', null, null, '0'), ('12', 'user_view', 'user', 'view', '查看', 'btn btn-xs btn-white | fa fa-eye bigger-120', '/user/view', '4', '3', null, 'user_view.html', '800*390', '1', null, null, '0'), ('13', 'user_audit', 'user', 'audit', '审核', 'btn btn-xs btn-white | fa fa-user  bigger-120', '{\"status\":\"3\"}', '5', '3', null, null, null, '1', null, null, '0'), ('14', 'user_audit_ok', 'user_audit', 'ok', '通过', 'btn btn-xs btn-white | fa fa-check  bigger-120', '/user/auditOk', '1', '4', null, null, null, '1', null, null, '0'), ('15', 'user_audit_refuse', 'user_audit', 'refuse', '拒绝', 'btn btn-xs btn-white | fa fa-times  bigger-120', '/user/auditRefuse', '2', '4', null, null, null, '1', null, null, '0'), ('16', 'user_audit_back', 'user_audit', 'back', '返回', 'btn btn-xs btn-white | fa fa-undo  bigger-120', null, '3', '4', null, null, null, '1', null, null, '0'), ('17', 'user_reset', 'user', 'reset', '重置密码', 'btn btn-xs btn-white | fa fa-key  bigger-120', '/user/reset', '6', '3', null, null, null, '1', null, null, '0'), ('18', 'user_ban', 'user', 'frozen', '冻结', 'btn btn-xs btn-white | fa fa-ban  bigger-120', '/user/ban', '7', '3', null, null, null, '1', null, null, '0'), ('19', 'user_recycle', 'user', 'recycle', '回收站', 'btn btn-xs btn-white | fa fa-recycle  bigger-120', '{\"status\":\"5\"}', '8', '3', null, null, null, '1', null, null, '0'), ('20', 'user_recycle_restore', 'user_recycle', 'restore', '还原', 'btn btn-xs btn-white | fa fa-refresh  bigger-120', '/user/restore', '1', '4', null, null, null, '1', null, null, '0'), ('21', 'user_recycle_remove', 'user_recycle', 'remove', '彻底删除', 'btn btn-xs btn-white  btn-danger | fa fa fa-times bigger-120', '/user/remove', '2', '4', null, null, null, '1', null, null, '0'), ('22', 'user_recycle_back', 'user_recycle', 'back', '返回', 'btn btn-xs btn-white | fa fa-undo  bigger-120', null, '3', '4', null, null, null, '1', null, null, '0'), ('23', 'user_roleAssign', 'user', 'assign', '角色分配', 'btn btn-xs btn-white | fa fa-users bigger-120', '/user/roleAssign', '9', '3', null, 'user_roleAssign.html', '350*500', '1', null, null, '0'), ('24', 'user_extrole', 'user', 'agent', '权限代理', 'btn btn-xs btn-white | fa fa-wrench  bigger-120', '/user/extrole', '10', '3', null, 'user_extrole.html', null, '1', null, null, '0'), ('25', 'menu', 'system', null, '菜单管理', 'fa-tasks', '/menu/', '3', '2', null, null, null, '1', null, null, '0'), ('26', 'menu_add', 'menu', 'addex', '菜单新增', 'btn btn-xs btn-white | fa fa-floppy-o bigger-120', '/menu/add', '1', '3', null, 'menu_add.html', '800*430', '1', '0', '0', '1'), ('27', 'menu_edit', 'menu', 'edit', '修改', 'btn btn-xs btn-white | fa fa-pencil  bigger-120', '/menu/edit', '2', '3', null, 'menu_edit.html', '800*430', '1', '0', '0', '1'), ('28', 'menu_del', 'menu', 'remove', '删除', 'btn btn-xs btn-white | fa fa-times  bigger-120', '/menu/del', '3', '3', null, null, null, '1', '0', null, '1'), ('29', 'menu_view', 'menu', 'view', '查看', 'btn btn-xs btn-white | fa fa-eye bigger-120', '/menu/view', '4', '3', null, 'menu_view.html', '800*430', '1', '0', '0', '1'), ('30', 'menu_recycle', 'menu', 'recycle', '回收站', 'btn btn-xs btn-white | fa fa-recycle  bigger-120', '{\"status\":\"2\"}', '5', '3', null, null, null, '1', null, null, '0'), ('31', 'menu_recycle_restore', 'menu_recycle', 'restore', '还原', 'btn btn-xs btn-white | fa fa-refresh  bigger-120', '/menu/restore', '1', '4', null, null, null, '1', null, null, '0'), ('32', 'menu_recycle_remove', 'menu_recycle', 'remove', '彻底删除', 'btn btn-xs btn-white  btn-danger | fa fa fa-times bigger-120', '/menu/remove', '2', '4', null, null, null, '1', '0', null, '1'), ('33', 'menu_recycle_back', 'menu_recycle', 'back', '返回', 'btn btn-xs btn-white | fa fa-undo  bigger-120', null, '3', '4', null, null, null, '1', null, null, '0'), ('34', 'dict', 'system', null, '字典管理', 'fa fa-book', '/dict/', '4', '2', null, null, null, '1', null, null, '0'), ('35', 'dict_add', 'dict', 'addex', '字典新增', 'btn btn-xs btn-white | fa fa-floppy-o bigger-120', '/dict/add', '1', '3', null, 'dict_add.html', '800*390', '1', '0', null, '2'), ('36', 'dict_edit', 'dict', 'edit', '修改', 'btn btn-xs btn-white | fa fa-pencil  bigger-120', '/dict/edit', '2', '3', null, 'dict_edit.html', '800*390', '1', null, null, '0'), ('37', 'dict_remove', 'dict', 'remove', '删除', 'btn btn-xs btn-white | fa fa-times  bigger-120', '/dict/remove', '3', '3', null, null, null, '1', null, null, '0'), ('38', 'dict_view', 'dict', 'view', '查看', 'btn btn-xs btn-white | fa fa-eye bigger-120', '/dict/view', '4', '3', null, 'dict_view.html', '800*390', '1', null, null, '0'), ('39', 'dept', 'system', null, '部门管理', 'fa fa-users', '/dept/', '5', '2', null, null, null, '1', null, null, '0'), ('40', 'dept_add', 'dept', 'addex', '部门新增', 'btn btn-xs btn-white | fa fa-floppy-o bigger-120', '/dept/add', '1', '3', null, 'dept_add.html', '800*340', '1', null, null, '0'), ('41', 'dept_edit', 'dept', 'edit', '修改', 'btn btn-xs btn-white | fa fa-pencil  bigger-120', '/dept/edit', '2', '3', null, 'dept_edit.html', '800*340', '1', null, null, '0'), ('42', 'dept_remove', 'dept', 'remove', '删除', 'btn btn-xs btn-white | fa fa-times  bigger-120', '/dept/remove', '3', '3', null, null, null, '1', null, null, '0'), ('43', 'dept_view', 'dept', 'view', '查看', 'btn btn-xs btn-white | fa fa-eye bigger-120', '/dept/view', '4', '3', null, 'dept_view.html', '800*340', '1', '0', '0', '0'), ('44', 'attach', 'system', null, '附件管理', 'fa fa-paperclip', '/attach/', '6', '2', null, 'attach.html', null, '1', '0', '0', '0'), ('45', 'attach_add', 'attach', 'add', '新增', 'btn btn-xs btn-white | fa fa-floppy-o bigger-120', '/attach/add', '1', '3', null, 'attach_add.html', '800*450', '1', '0', '0', '0'), ('46', 'attach_edit', 'attach', 'edit', '修改', 'btn btn-xs btn-white | fa fa-pencil  bigger-120', '/attach/edit', '2', '3', null, 'attach_edit.html', '800*290', '1', '0', null, '0'), ('47', 'attach_remove', 'attach', 'remove', '删除', 'btn btn-xs btn-white | fa fa-times  bigger-120', '/attach/remove', '3', '3', null, null, null, '1', null, null, '0'), ('48', 'attach_view', 'attach', 'view', '查看', 'btn btn-xs btn-white | fa fa-eye bigger-120', '/attach/view', '4', '3', null, 'attach_view.html', '800*450', '1', '0', '0', '1'), ('49', 'attach_download', 'attach', 'download', '下载', 'btn btn-xs btn-white | fa fa-paperclip bigger-120', '/attach/download', '5', '3', null, null, null, '1', null, null, '0'), ('56', 'parameter', 'system', null, '参数化管理', 'fa-tags', '/parameter/', '9', '2', null, 'parameter.html', null, '1', '0', '1', '0'), ('57', 'parameter_add', 'parameter', 'add', '新增', 'btn btn-xs btn-white | fa fa-floppy-o bigger-120', '/parameter/add', '1', '3', null, 'parameter_add.html', null, '1', '0', '0', '0'), ('58', 'parameter_edit', 'parameter', 'edit', '修改', 'btn btn-xs btn-white | fa fa-pencil  bigger-120', '/parameter/edit', '2', '3', null, 'parameter_edit.html', null, '1', '0', '0', '0'), ('59', 'parameter_del', 'parameter', 'remove', '删除', 'btn btn-xs btn-white | fa fa-times  bigger-120', '/parameter/del', '3', '3', null, null, null, '1', '0', '0', '1'), ('60', 'parameter_view', 'parameter', 'view', '查看', 'btn btn-xs btn-white | fa fa-eye bigger-120', '/parameter/view', '4', '3', null, 'parameter_view.html', null, '1', '0', '0', '0'), ('61', 'parameter_recycle', 'parameter', 'recycle', '回收站', 'btn btn-xs btn-white | fa fa-recycle  bigger-120', '{\"status\":\"5\"}', '5', '3', null, 'parameter_recycle.html', null, '1', '0', '0', '0'), ('62', 'parameter_recycle_restore', 'parameter_recycle', 'restore', '还原', 'btn btn-xs btn-white | fa fa-refresh  bigger-120', '/parameter/restore', '1', '4', null, null, null, '1', '0', '0', '0'), ('63', 'parameter_recycle_remove', 'parameter_recycle', 'remove', '彻底删除', 'btn btn-xs btn-white  btn-danger | fa fa fa-times bigger-120', '/parameter/remove', '2', '4', null, null, null, '1', '0', '0', '1'), ('64', 'parameter_recycle_back', 'parameter_recycle', 'back', '返回', 'btn btn-xs btn-white | fa fa-undo  bigger-120', null, '3', '4', null, null, null, '1', '0', '0', '0'), ('65', 'druid', 'system', null, '连接池监视', 'fa-arrows-v', '/druid', '10', '2', null, null, null, '1', '0', null, '1'), ('81', 'log', 'system', null, '日志管理', 'fa-tasks', null, '11', '2', null, null, null, '1', '0', '0', '1'), ('82', 'olog', 'log', null, '操作日志', 'fa-database', '/olog/', '1', '3', null, 'olog.html', null, '1', '0', '0', '0'), ('83', 'llog', 'log', null, '登录日志', 'fa-sign-in', '/llog/', '2', '3', null, 'llog.html', null, '1', '0', '1', '0'), ('84', 'olog_add', 'olog', 'add', '新增', 'btn btn-xs btn-white | fa fa-floppy-o bigger-120', '/olog/add', '1', '4', null, 'olog_add.html', null, '1', '0', '0', '0'), ('85', 'olog_edit', 'olog', 'edit', '修改', 'btn btn-xs btn-white | fa fa-pencil  bigger-120', '/olog/edit', '2', '4', null, 'olog_edit.html', null, '1', '0', '0', '0'), ('86', 'olog_remove', 'olog', 'remove', '删除', 'btn btn-xs btn-white | fa fa-times  bigger-120', '/olog/remove', '3', '4', null, null, null, '1', '0', '0', '0'), ('87', 'olog_view', 'olog', 'view', '查看', 'btn btn-xs btn-white | fa fa-eye bigger-120', '/olog/view', '4', '4', null, 'olog_view.html', null, '1', '0', '0', '0'), ('88', 'llog_add', 'llog', 'add', '新增', 'btn btn-xs btn-white | fa fa-floppy-o bigger-120', '/llog/add', '1', '4', null, 'llog_add.html', null, '1', '0', '0', '0'), ('89', 'llog_edit', 'llog', 'edit', '修改', 'btn btn-xs btn-white | fa fa-pencil  bigger-120', '/llog/edit', '2', '4', null, 'llog_edit.html', null, '1', '0', '0', '0'), ('90', 'llog_remove', 'llog', 'remove', '删除', 'btn btn-xs btn-white | fa fa-times  bigger-120', '/llog/remove', '3', '4', null, null, null, '1', '0', '0', '0'), ('91', 'llog_view', 'llog', 'view', '查看', 'btn btn-xs btn-white | fa fa-eye bigger-120', '/llog/view', '4', '4', null, 'llog_view.html', null, '1', '0', '0', '0'), ('92', 'office', '0', null, '工作台', 'fa fa-desktop', null, '1', '1', null, null, null, '1', '0', '0', '0'), ('93', 'notice', 'office', null, '通知公告', 'fa fa-bell', '/notice/', '1', '2', null, null, null, '1', '0', '0', '0'), ('94', 'notice_add', 'notice', 'add', '新增', 'btn btn-xs btn-white | fa fa-floppy-o bigger-120', '/notice/add', '1', '3', null, null, '800*500', '1', '0', '0', '1'), ('95', 'notice_edit', 'notice', 'edit', '修改', 'btn btn-xs btn-white | fa fa-pencil  bigger-120', '/notice/edit', '2', '3', null, null, '800*500', '1', '0', '0', '0'), ('96', 'notice_remove', 'notice', 'remove', '删除', 'btn btn-xs btn-white | fa fa-times  bigger-120', '/notice/remove', '3', '3', null, null, null, '1', '0', '0', '0'), ('97', 'notice_view', 'notice', 'view', '查看', 'btn btn-xs btn-white | fa fa-eye bigger-120', '/notice/view', '4', '3', null, null, '800*500', '1', '0', '0', '0'), ('98', 'online', 'system', '', '在线开发', 'fa-rocket', null, '12', '2', null, null, '800*520', '1', '0', null, '1'), ('99', 'generate', 'online', null, '代码生成', 'fa-gavel', '/generate/', '1', '3', null, null, '800*520', '1', '0', null, '1'), ('100', 'generate_add', 'generate', 'add', '新增', 'btn btn-xs btn-white | fa fa-floppy-o bigger-120', '/generate/add', '1', '4', null, null, '800*420', '1', '0', null, '3'), ('101', 'generate_edit', 'generate', 'edit', '修改', 'btn btn-xs btn-white | fa fa-pencil  bigger-120', '/generate/edit', '2', '4', null, null, '800*420', '1', '0', null, '3'), ('102', 'generate_remove', 'generate', 'remove', '删除', 'btn btn-xs btn-white | fa fa-times  bigger-120', '/generate/remove', '3', '4', null, null, '800*520', '1', '0', null, null), ('103', 'generate_view', 'generate', 'view', '查看', 'btn btn-xs btn-white | fa fa-eye bigger-120', '/generate/view', '4', '4', null, null, '800*420', '1', '0', null, '3'), ('104', 'generate_gencode', 'generate', 'gencode', '代码生成', 'btn btn-xs btn-white | fa fa-gavel bigger-120', '/generate/gencode', '5', '4', null, null, '800*520', '1', '0', null, '1');
COMMIT;

-- ----------------------------
-- Table structure for tfw_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `tfw_operation_log`;
CREATE TABLE `tfw_operation_log` (
`ID`  int(65) NOT NULL AUTO_INCREMENT ,
`LOGNAME`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`USERID`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`CLASSNAME`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`METHOD`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
`CREATETIME`  datetime NULL DEFAULT NULL ,
`SUCCEED`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`MESSAGE`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
PRIMARY KEY (`ID`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=30

;

-- ----------------------------
-- Records of tfw_operation_log
-- ----------------------------
BEGIN;
INSERT INTO `tfw_operation_log` VALUES ('1', '异常日志', '1', null, 'Missing URI template variable \'tablePath\' for method parameter of type String', '2016-11-01 09:29:09', '0', null), ('2', '异常日志', '1', null, 'Missing URI template variable \'tablePath\' for method parameter of type String', '2016-11-01 09:29:17', '0', null), ('3', '异常日志', '1', null, 'Missing URI template variable \'tablePath\' for method parameter of type String', '2016-11-01 09:29:37', '0', null), ('4', '异常日志', '1', null, 'Missing URI template variable \'tablePath\' for method parameter of type String', '2016-11-01 09:29:56', '0', null), ('5', '异常日志', '1', null, '在 /beetlsql/mysql/userMapper.sql 和 /beetlsql/mysql/userMapper.md 和 /beetlsql/userMapper.sql 和 /beetlsql/userMapper.md 和  未找到[id=userMapper.mysqllist]相关的SQL', '2016-11-03 16:57:48', '0', null), ('6', '异常日志', '1', null, 'com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException: Table \'blade.tmsp_role\' doesn\'t exist', '2016-11-03 16:58:24', '0', null), ('7', '异常日志', '1', null, 'table \"tb_tfw_tzgg\" not exist', '2016-11-04 15:20:20', '0', null), ('8', '异常日志', '1', null, 'syntax error, expect {, actual EOF, pos 0, json : ', '2016-11-07 11:13:11', '0', null), ('9', '异常日志', '1', null, 'syntax error, expect {, actual EOF, pos 0, json : ', '2016-11-07 11:13:16', '0', null), ('10', '异常日志', '1', null, 'syntax error, expect {, actual EOF, pos 0, json : ', '2016-11-07 11:14:07', '0', null), ('11', '异常日志', '1', null, 'syntax error, expect {, actual EOF, pos 0, json : ', '2016-11-07 11:15:37', '0', null), ('12', '异常日志', '1', null, 'com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException: Unknown column \'pId\' in \'field list\'', '2016-11-07 13:36:01', '0', null), ('13', '异常日志', '1', null, 'com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException: Unknown column \'pId\' in \'field list\'', '2016-11-07 13:37:07', '0', null), ('14', '异常日志', '1', null, 'com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException: Unknown column \'pId\' in \'field list\'', '2016-11-07 13:37:10', '0', null), ('15', '异常日志', '1', null, 'com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException: Unknown column \'pId\' in \'field list\'', '2016-11-07 13:37:54', '0', null), ('16', '异常日志', '1', null, 'Inject [pid] error!', '2016-11-07 15:15:15', '0', null), ('17', '异常日志', '1', null, 'com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException: Unknown column \'diy_dict.diy\' in \'where clause\'', '2016-11-07 15:19:13', '0', null), ('18', '异常日志', '1', null, 'com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException: Unknown column \'diy_dict.diy\' in \'where clause\'', '2016-11-07 15:19:18', '0', null), ('19', '异常日志', '1', null, 'com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException: Unknown column \'diy_dict.diy\' in \'where clause\'', '2016-11-07 15:20:06', '0', null), ('20', '异常日志', '1', null, 'com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException: Unknown column \'diy_dict.diy\' in \'where clause\'', '2016-11-07 15:20:25', '0', null), ('21', '异常日志', '1', null, 'com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException: Unknown column \'diy_dict.diy\' in \'where clause\'', '2016-11-07 15:22:42', '0', null), ('22', '异常日志', '1', null, 'com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException: Unknown column \'diy_dict.diy\' in \'where clause\'', '2016-11-07 15:23:08', '0', null), ('23', '异常日志', '1', null, 'com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException: Unknown column \'diy_dict.diy\' in \'where clause\'', '2016-11-07 15:23:37', '0', null), ('24', '异常日志', '1', null, 'java.sql.SQLException: The used SELECT statements have a different number of columns', '2016-11-07 15:27:05', '0', null), ('25', '异常日志', '1', null, 'String index out of range: -1', '2016-11-07 16:30:33', '0', null), ('26', '异常日志', '1', null, 'String index out of range: -1', '2016-11-07 16:31:24', '0', null), ('27', '异常日志', '1', null, 'String index out of range: -1', '2016-11-07 16:32:01', '0', null), ('28', '异常日志', '1', null, 'Handler dispatch failed; nested exception is java.lang.NoSuchMethodError: com.smallchill.system.controller.CacheController$9.<init>(Lcom/smallchill/system/controller/CacheController;)V', '2016-11-07 17:08:16', '0', null), ('29', '异常日志', '1', null, 'Handler dispatch failed; nested exception is java.lang.NoSuchMethodError: com.smallchill.system.controller.CacheController$11.<init>(Lcom/smallchill/system/controller/CacheController;)V', '2016-11-07 17:08:20', '0', null);
COMMIT;

-- ----------------------------
-- Table structure for tfw_parameter
-- ----------------------------
DROP TABLE IF EXISTS `tfw_parameter`;
CREATE TABLE `tfw_parameter` (
`ID`  int(11) NOT NULL AUTO_INCREMENT ,
`CODE`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`NUM`  int(11) NULL DEFAULT NULL ,
`NAME`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`PARA`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
`TIPS`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`STATUS`  int(11) NULL DEFAULT NULL ,
`VERSION`  int(11) NULL DEFAULT NULL ,
PRIMARY KEY (`ID`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=2

;

-- ----------------------------
-- Records of tfw_parameter
-- ----------------------------
BEGIN;
INSERT INTO `tfw_parameter` VALUES ('1', '101', '100', '是否开启记录日志', '2', '1:是  2:否', '1', '7');
COMMIT;

-- ----------------------------
-- Table structure for tfw_relation
-- ----------------------------
DROP TABLE IF EXISTS `tfw_relation`;
CREATE TABLE `tfw_relation` (
`ID`  int(11) NOT NULL AUTO_INCREMENT ,
`MENUID`  int(11) NULL DEFAULT NULL ,
`ROLEID`  int(11) NULL DEFAULT NULL ,
PRIMARY KEY (`ID`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=2097

;

-- ----------------------------
-- Records of tfw_relation
-- ----------------------------
BEGIN;
INSERT INTO `tfw_relation` VALUES ('821', '73', '2'), ('822', '74', '2'), ('823', '75', '2'), ('824', '76', '2'), ('825', '77', '2'), ('826', '78', '2'), ('827', '79', '2'), ('828', '80', '2'), ('829', '81', '2'), ('830', '82', '2'), ('831', '83', '2'), ('832', '84', '2'), ('833', '85', '2'), ('834', '86', '2'), ('835', '87', '2'), ('836', '88', '2'), ('837', '89', '2'), ('838', '90', '2'), ('839', '91', '2'), ('840', '92', '2'), ('841', '93', '2'), ('842', '94', '2'), ('843', '95', '2'), ('844', '96', '2'), ('845', '97', '2'), ('846', '98', '2'), ('847', '99', '2'), ('848', '100', '2'), ('849', '101', '2'), ('850', '102', '2'), ('851', '1', '2'), ('852', '8', '2'), ('853', '9', '2'), ('854', '10', '2'), ('855', '11', '2'), ('856', '12', '2'), ('857', '13', '2'), ('858', '14', '2'), ('859', '15', '2'), ('860', '16', '2'), ('861', '17', '2'), ('862', '18', '2'), ('863', '19', '2'), ('864', '20', '2'), ('865', '21', '2'), ('866', '22', '2'), ('867', '23', '2'), ('868', '24', '2'), ('869', '2', '2'), ('870', '3', '2'), ('871', '4', '2'), ('872', '5', '2'), ('873', '6', '2'), ('874', '7', '2'), ('875', '25', '2'), ('876', '26', '2'), ('877', '27', '2'), ('878', '28', '2'), ('879', '29', '2'), ('880', '30', '2'), ('881', '31', '2'), ('882', '32', '2'), ('883', '33', '2'), ('884', '34', '2'), ('885', '35', '2'), ('886', '36', '2'), ('887', '37', '2'), ('888', '38', '2'), ('889', '39', '2'), ('890', '40', '2'), ('891', '41', '2'), ('892', '42', '2'), ('893', '43', '2'), ('894', '44', '2'), ('895', '45', '2'), ('896', '46', '2'), ('897', '47', '2'), ('898', '48', '2'), ('899', '49', '2'), ('1524', '1', '25'), ('1525', '62', '25'), ('1526', '64', '25'), ('1527', '72', '25'), ('1528', '73', '25'), ('1529', '74', '25'), ('1530', '75', '25'), ('1531', '76', '25'), ('1532', '77', '25'), ('1533', '78', '25'), ('1534', '79', '25'), ('1535', '80', '25'), ('1893', '92', '1'), ('1894', '93', '1'), ('1895', '94', '1'), ('1896', '95', '1'), ('1897', '96', '1'), ('1898', '97', '1'), ('1899', '98', '1'), ('1900', '99', '1'), ('1901', '100', '1');
INSERT INTO `tfw_relation` VALUES ('1902', '101', '1'), ('1903', '102', '1'), ('1904', '103', '1'), ('1905', '104', '1'), ('1906', '105', '1'), ('1907', '106', '1'), ('1908', '107', '1'), ('1909', '225', '1'), ('1910', '1', '1'), ('1911', '8', '1'), ('1912', '9', '1'), ('1913', '10', '1'), ('1914', '11', '1'), ('1915', '12', '1'), ('1916', '13', '1'), ('1917', '14', '1'), ('1918', '15', '1'), ('1919', '16', '1'), ('1920', '17', '1'), ('1921', '18', '1'), ('1922', '19', '1'), ('1923', '20', '1'), ('1924', '21', '1'), ('1925', '22', '1'), ('1926', '23', '1'), ('1927', '24', '1'), ('1928', '2', '1'), ('1929', '3', '1'), ('1930', '4', '1'), ('1931', '5', '1'), ('1932', '6', '1'), ('1933', '7', '1'), ('1934', '25', '1'), ('1935', '26', '1'), ('1936', '27', '1'), ('1937', '28', '1'), ('1938', '29', '1'), ('1939', '30', '1'), ('1940', '31', '1'), ('1941', '32', '1'), ('1942', '33', '1'), ('1943', '34', '1'), ('1944', '35', '1'), ('1945', '36', '1'), ('1946', '37', '1'), ('1947', '38', '1'), ('1948', '39', '1'), ('1949', '40', '1'), ('1950', '41', '1'), ('1951', '42', '1'), ('1952', '43', '1'), ('1953', '44', '1'), ('1954', '45', '1'), ('1955', '46', '1'), ('1956', '47', '1'), ('1957', '48', '1'), ('1958', '49', '1'), ('1959', '56', '1'), ('1960', '57', '1'), ('1961', '58', '1'), ('1962', '59', '1'), ('1963', '60', '1'), ('1964', '61', '1'), ('1965', '62', '1'), ('1966', '63', '1'), ('1967', '64', '1'), ('1968', '65', '1'), ('1969', '81', '1'), ('1970', '82', '1'), ('1971', '84', '1'), ('1972', '85', '1'), ('1973', '86', '1'), ('1974', '87', '1'), ('1975', '83', '1'), ('1976', '88', '1'), ('1977', '89', '1'), ('1978', '90', '1'), ('1979', '91', '1'), ('1998', '1', '4'), ('1999', '2', '4'), ('2000', '3', '4'), ('2001', '4', '4'), ('2002', '5', '4'), ('2003', '6', '4'), ('2004', '7', '4'), ('2005', '39', '4'), ('2006', '40', '4'), ('2007', '41', '4'), ('2008', '42', '4'), ('2009', '43', '4'), ('2010', '81', '4'), ('2011', '82', '4'), ('2012', '84', '4'), ('2013', '85', '4'), ('2014', '86', '4'), ('2015', '87', '4'), ('2016', '83', '4'), ('2017', '88', '4'), ('2018', '89', '4'), ('2019', '90', '4');
INSERT INTO `tfw_relation` VALUES ('2020', '91', '4'), ('2029', '1', '5'), ('2030', '39', '5'), ('2031', '40', '5'), ('2032', '41', '5'), ('2033', '42', '5'), ('2034', '43', '5'), ('2035', '81', '5'), ('2036', '82', '5'), ('2037', '84', '5'), ('2038', '85', '5'), ('2039', '86', '5'), ('2040', '87', '5'), ('2075', '1', '6'), ('2076', '81', '6'), ('2077', '82', '6'), ('2078', '84', '6'), ('2079', '85', '6'), ('2080', '86', '6'), ('2081', '87', '6'), ('2082', '83', '6'), ('2083', '88', '6'), ('2084', '89', '6'), ('2085', '90', '6'), ('2086', '91', '6'), ('2090', '1', '3'), ('2091', '56', '3'), ('2092', '61', '3'), ('2093', '62', '3'), ('2094', '64', '3');
COMMIT;

-- ----------------------------
-- Table structure for tfw_role
-- ----------------------------
DROP TABLE IF EXISTS `tfw_role`;
CREATE TABLE `tfw_role` (
`ID`  int(11) NOT NULL AUTO_INCREMENT ,
`NUM`  int(11) NULL DEFAULT NULL ,
`PID`  int(11) NULL DEFAULT NULL ,
`NAME`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`DEPTID`  int(11) NULL DEFAULT NULL ,
`TIPS`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`VERSION`  int(11) NULL DEFAULT NULL ,
PRIMARY KEY (`ID`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=7

;

-- ----------------------------
-- Records of tfw_role
-- ----------------------------
BEGIN;
INSERT INTO `tfw_role` VALUES ('1', '1', '0', '超级管理员', '1', 'administrator', '1'), ('2', '1', '1', '管理员', '7', 'admin', '3'), ('3', '2', '1', '管理员1', '10', 'admin', '2'), ('4', '2', '0', '测试', '10', 'test', '1'), ('5', '1', '4', '测试1', '3', 'test', '2'), ('6', '2', '4', '测试2', '10', 'test', '1');
COMMIT;

-- ----------------------------
-- Table structure for tfw_role_ext
-- ----------------------------
DROP TABLE IF EXISTS `tfw_role_ext`;
CREATE TABLE `tfw_role_ext` (
`ID`  int(11) NOT NULL AUTO_INCREMENT ,
`USERID`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`ROLEIN`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
`ROLEOUT`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL ,
PRIMARY KEY (`ID`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=128

;

-- ----------------------------
-- Records of tfw_role_ext
-- ----------------------------
BEGIN;
INSERT INTO `tfw_role_ext` VALUES ('27', '66', '1,44,49', '45'), ('47', '2', '0', '8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24'), ('48', '63', '0', '0'), ('49', '72', '0', '0'), ('50', '74', '0', '0'), ('67', '1', '0', '0'), ('87', '168', '92,103,104,105,106,107', '109,110,111,112,113,114,115,116,117,118,119,120,121,122'), ('107', '189', '108,109,110,111,112,113,114,115,116,117,118,119,120,121,122', '0'), ('127', '21', '92,98,99,100,101,102,1,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,39,40,41,42,43', '0');
COMMIT;

-- ----------------------------
-- Table structure for tfw_user
-- ----------------------------
DROP TABLE IF EXISTS `tfw_user`;
CREATE TABLE `tfw_user` (
`ID`  int(11) NOT NULL AUTO_INCREMENT ,
`ACCOUNT`  varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`PASSWORD`  varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`SALT`  varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`NAME`  varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`BIRTHDAY`  datetime NULL DEFAULT NULL ,
`SEX`  int(11) NULL DEFAULT NULL ,
`EMAIL`  varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`PHONE`  varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`ROLEID`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`DEPTID`  int(11) NULL DEFAULT NULL ,
`STATUS`  int(11) NULL DEFAULT NULL ,
`CREATETIME`  datetime NULL DEFAULT NULL ,
`VERSION`  int(11) NULL DEFAULT NULL ,
PRIMARY KEY (`ID`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=24

;

-- ----------------------------
-- Records of tfw_user
-- ----------------------------
BEGIN;
INSERT INTO `tfw_user` VALUES ('1', 'admin', '4779e4a9903dfb08f9f877791c516a73', 'admin', '管理员', '2015-09-08 00:00:00', '1', 'admin@tonbusoft.com.cn', '222333', '1', '9', '1', '2016-01-29 08:49:53', '24'), ('21', 'test001', 'e334680512284cac2f57701abe03af96', 'r4i90', 'test', '2016-02-19 14:00:13', '1', ' ', ' ', '5', '3', '1', '2016-02-19 14:00:19', '25'), ('22', '123123', '653f21c93acdd4f03c95876824f440a7', '048wh', '213123', '2016-05-03 00:00:00', '1', '1232', '1232', '4', '1', '1', '2016-05-17 18:50:15', '3');
COMMIT;

-- ----------------------------
-- Function structure for queryChildren
-- ----------------------------
DROP FUNCTION IF EXISTS `queryChildren`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `queryChildren`(rootid VARCHAR(500),tabname VARCHAR(500)) RETURNS varchar(4000) CHARSET utf8
BEGIN
DECLARE sTemp VARCHAR(4000);
DECLARE sTempChd VARCHAR(4000);
DECLARE icount INTEGER;
DECLARE tname VARCHAR(500);

SET sTemp = '$';
SET sTempChd = rootid;
set tname=tabname;

if INSTR(tname,'tfw_dept')>0 then
select count(1) into icount from tfw_dept where id=sTempChd;
if icount>0 then
WHILE sTempChd is not NULL DO
if sTempChd <> rootid then 
SET sTemp = CONCAT(sTemp,',',sTempChd);
end if;
SELECT group_concat(id) INTO sTempChd FROM tfw_dept where FIND_IN_SET(pid,sTempChd)>0;
END WHILE;
RETURN SUBSTRING(sTemp,3);
ELSE
RETURN null;
end if;
end if;
if INSTR(tname,'tfw_role')>0 then
select count(1) into icount from tfw_role where id=sTempChd;
if icount>0 then
WHILE sTempChd is not NULL DO
if sTempChd <> rootid then 
SET sTemp = CONCAT(sTemp,',',sTempChd);
end if;
SELECT group_concat(id) INTO sTempChd FROM tfw_role where FIND_IN_SET(pid,sTempChd)>0;
END WHILE;
RETURN SUBSTRING(sTemp,3);
ELSE
RETURN null;
end if;
end if;


END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for queryParent
-- ----------------------------
DROP FUNCTION IF EXISTS `queryParent`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `queryParent`(rootid VARCHAR(500),tabname VARCHAR(500)) RETURNS varchar(4000) CHARSET utf8
BEGIN
DECLARE sTemp VARCHAR(4000);
DECLARE sTempChd VARCHAR(4000);
DECLARE icount INTEGER;
DECLARE tname VARCHAR(500);

SET sTemp = '$';
SET sTempChd = rootid;
set tname=tabname;

if INSTR(tname,'tfw_dept')>0 then
select count(1) into icount from tfw_dept where id=sTempChd;
if icount>0 then
WHILE sTempChd is not NULL DO
if sTempChd <> rootid then 
SET sTemp = CONCAT(sTemp,',',sTempChd);
end if;
SELECT group_concat(pid) INTO sTempChd FROM tfw_dept where FIND_IN_SET(id,sTempChd)>0;
END WHILE;
RETURN SUBSTRING(sTemp,3);
ELSE
RETURN null;
end if;
end if;
if INSTR(tname,'tfw_role')>0 then
select count(1) into icount from tfw_role where id=sTempChd;
if icount>0 then
WHILE sTempChd is not NULL DO
if sTempChd <> rootid then 
SET sTemp = CONCAT(sTemp,',',sTempChd);
end if;
SELECT group_concat(pid) INTO sTempChd FROM tfw_role where FIND_IN_SET(id,sTempChd)>0;
END WHILE;
RETURN SUBSTRING(sTemp,3);
ELSE
RETURN null;
end if;
end if;


END
;;
DELIMITER ;

-- ----------------------------
-- Auto increment value for tb_yw_tzgg
-- ----------------------------
ALTER TABLE `tb_yw_tzgg` AUTO_INCREMENT=6;

-- ----------------------------
-- Auto increment value for tfw_attach
-- ----------------------------
ALTER TABLE `tfw_attach` AUTO_INCREMENT=323;

-- ----------------------------
-- Auto increment value for tfw_dept
-- ----------------------------
ALTER TABLE `tfw_dept` AUTO_INCREMENT=14;

-- ----------------------------
-- Auto increment value for tfw_dict
-- ----------------------------
ALTER TABLE `tfw_dict` AUTO_INCREMENT=46;

-- ----------------------------
-- Auto increment value for tfw_generate
-- ----------------------------
ALTER TABLE `tfw_generate` AUTO_INCREMENT=2;

-- ----------------------------
-- Auto increment value for tfw_login_log
-- ----------------------------
ALTER TABLE `tfw_login_log` AUTO_INCREMENT=1;

-- ----------------------------
-- Auto increment value for tfw_menu
-- ----------------------------
ALTER TABLE `tfw_menu` AUTO_INCREMENT=105;

-- ----------------------------
-- Auto increment value for tfw_operation_log
-- ----------------------------
ALTER TABLE `tfw_operation_log` AUTO_INCREMENT=30;

-- ----------------------------
-- Auto increment value for tfw_parameter
-- ----------------------------
ALTER TABLE `tfw_parameter` AUTO_INCREMENT=2;

-- ----------------------------
-- Auto increment value for tfw_relation
-- ----------------------------
ALTER TABLE `tfw_relation` AUTO_INCREMENT=2097;

-- ----------------------------
-- Auto increment value for tfw_role
-- ----------------------------
ALTER TABLE `tfw_role` AUTO_INCREMENT=7;

-- ----------------------------
-- Auto increment value for tfw_role_ext
-- ----------------------------
ALTER TABLE `tfw_role_ext` AUTO_INCREMENT=128;

-- ----------------------------
-- Auto increment value for tfw_user
-- ----------------------------
ALTER TABLE `tfw_user` AUTO_INCREMENT=24;
