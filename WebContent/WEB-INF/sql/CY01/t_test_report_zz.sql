/*
MySQL Data Transfer
Source Host: localhost
Source Database: msdb
Target Host: localhost
Target Database: msdb
Date: 2015-07-13 21:23:29
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for t_test_report_zz
-- ----------------------------
CREATE TABLE `t_test_report_zz` (
  `contractid` varchar(255) NOT NULL DEFAULT '',
  `sys1` varchar(255) NOT NULL,
  `sys2` varchar(255) DEFAULT NULL,
  `sys3` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`contractid`,`sys1`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
