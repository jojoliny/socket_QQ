/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50713
Source Host           : localhost:3306
Source Database       : qq_1701

Target Server Type    : MYSQL
Target Server Version : 50713
File Encoding         : 65001

Date: 2018-07-03 19:24:52
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for qq_friends
-- ----------------------------
DROP TABLE IF EXISTS `qq_friends`;
CREATE TABLE `qq_friends` (
  `no` int(11) NOT NULL AUTO_INCREMENT,
  `srcQq` varchar(12) NOT NULL,
  `destQq` varchar(12) NOT NULL,
  PRIMARY KEY (`no`,`srcQq`,`destQq`),
  KEY `srcQq` (`srcQq`),
  KEY `destQq` (`destQq`),
  CONSTRAINT `destQq` FOREIGN KEY (`destQq`) REFERENCES `qq_user` (`id`),
  CONSTRAINT `srcQq` FOREIGN KEY (`srcQq`) REFERENCES `qq_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qq_friends
-- ----------------------------
INSERT INTO `qq_friends` VALUES ('36', '1111', '6666');
INSERT INTO `qq_friends` VALUES ('39', '1111', '1234');
INSERT INTO `qq_friends` VALUES ('1', '1234', '6666');
INSERT INTO `qq_friends` VALUES ('3', '1234', '9999');
INSERT INTO `qq_friends` VALUES ('8', '1234', '3333');
INSERT INTO `qq_friends` VALUES ('12', '1234', '5555');
INSERT INTO `qq_friends` VALUES ('15', '1234', '7777');
INSERT INTO `qq_friends` VALUES ('18', '1234', '2222');
INSERT INTO `qq_friends` VALUES ('38', '1234', '1111');
INSERT INTO `qq_friends` VALUES ('19', '2222', '1234');
INSERT INTO `qq_friends` VALUES ('22', '2222', '3333');
INSERT INTO `qq_friends` VALUES ('9', '3333', '1234');
INSERT INTO `qq_friends` VALUES ('23', '3333', '2222');
INSERT INTO `qq_friends` VALUES ('24', '3333', '4444');
INSERT INTO `qq_friends` VALUES ('25', '4444', '3333');
INSERT INTO `qq_friends` VALUES ('26', '4444', '5555');
INSERT INTO `qq_friends` VALUES ('28', '4444', '6666');
INSERT INTO `qq_friends` VALUES ('13', '5555', '1234');
INSERT INTO `qq_friends` VALUES ('27', '5555', '4444');
INSERT INTO `qq_friends` VALUES ('32', '5555', '8888');
INSERT INTO `qq_friends` VALUES ('2', '6666', '1234');
INSERT INTO `qq_friends` VALUES ('20', '6666', '7777');
INSERT INTO `qq_friends` VALUES ('29', '6666', '4444');
INSERT INTO `qq_friends` VALUES ('37', '6666', '1111');
INSERT INTO `qq_friends` VALUES ('14', '7777', '1234');
INSERT INTO `qq_friends` VALUES ('21', '7777', '6666');
INSERT INTO `qq_friends` VALUES ('30', '7777', '8888');
INSERT INTO `qq_friends` VALUES ('31', '8888', '7777');
INSERT INTO `qq_friends` VALUES ('33', '8888', '5555');
INSERT INTO `qq_friends` VALUES ('4', '9999', '6666');

-- ----------------------------
-- Table structure for qq_user
-- ----------------------------
DROP TABLE IF EXISTS `qq_user`;
CREATE TABLE `qq_user` (
  `id` varchar(12) NOT NULL,
  `username` varchar(10) NOT NULL,
  `pwd` varchar(15) NOT NULL,
  `sign` varchar(50) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qq_user
-- ----------------------------
INSERT INTO `qq_user` VALUES ('1111', '二郎神', '111', null, '2017-06-01');
INSERT INTO `qq_user` VALUES ('1234', 'lala', '111', null, null);
INSERT INTO `qq_user` VALUES ('2222', '二货', '222', '', '1999-02-02');
INSERT INTO `qq_user` VALUES ('3333', '33娘', '333', '静若处子，动若脱兔', null);
INSERT INTO `qq_user` VALUES ('4444', '疯一般的少年', '444', '天下唯我独帅', '2000-04-04');
INSERT INTO `qq_user` VALUES ('5555', '金木', '555', '东京食尸鬼', null);
INSERT INTO `qq_user` VALUES ('6666', '六神', '666', '神一般的存在', '1966-06-06');
INSERT INTO `qq_user` VALUES ('7777', '路飞', '777', '海贼王！我当定了！！', null);
INSERT INTO `qq_user` VALUES ('8888', '小埋', '888', '干物妹！小埋', null);
INSERT INTO `qq_user` VALUES ('9999', '九儿', '000', '九儿，可爱的妹妹', null);
INSERT INTO `qq_user` VALUES ('sss', 'sss', 'sss', null, '2017-07-04');
