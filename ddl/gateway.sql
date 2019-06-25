--
-- 创建DB: gateway
--
CREATE DATABASE IF NOT EXISTS `gateway` DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

USE `gateway`


--
-- 表结构: gw_app
--
CREATE TABLE IF NOT EXISTS `gw_app` (
`id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '网关应用ID(自增主键)',
`name` varchar(64) NOT NULL DEFAULT '' COMMENT '网关应用名称',
`prepath` varchar(128) NOT NULL DEFAULT '' COMMENT '公共前缀',
`remark` varchar(256) NOT NULL DEFAULT '' COMMENT '备注',
`owners` varchar(256) NOT NULL DEFAULT '' COMMENT '负责人',
`status` int unsigned NOT NULL DEFAULT '0' COMMENT '状态(0:离线,1:在线)',
`uid` mediumint(8) unsigned NOT NULL DEFAULT '0' COMMENT '创建用户ID',
`ctime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建日期',
`mtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
`isdel` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
PRIMARY KEY (`id`),
UNIQUE KEY `uk_prepath` (`prepath`),
KEY `idx_status_isdel` (`status`,`isdel`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='网关应用表';

--
-- 表数据
--
INSERT IGNORE INTO `gw_app` (`id`,`name`,`prepath`,`remark`,`owners`,`status`) VALUES
(1,'运维','/op','运维应用','admin','1');

--
-- 表结构: gw_app_env
--
CREATE TABLE IF NOT EXISTS `gw_app_env` (
`id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
`app_id` bigint unsigned NOT NULL DEFAULT '0' COMMENT '网关应用ID',
`name` varchar(64) NOT NULL DEFAULT '' COMMENT '环境名称(如:prod,test,dev)',
`url` varchar(256) NOT NULL DEFAULT '' COMMENT '服务URL',
`uid` mediumint(8) unsigned NOT NULL DEFAULT '0' COMMENT '创建用户ID',
`ctime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建日期',
`mtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
`isdel` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
PRIMARY KEY (`id`),
UNIQUE KEY `uk_app_id_name` (`app_id`,`name`),
KEY `idx_app_id` (`app_id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='网关应用服务环境表';

--
-- 表数据
--
INSERT IGNORE INTO `gw_app_env` (`id`,`app_id`,`name`,`url`) VALUES
(1,'1','prod','http://localhost');

--
-- 表结构: gw_api
--
CREATE TABLE IF NOT EXISTS `gw_api` (
`id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '接口ID(自增主键)',
`app_id` bigint unsigned NOT NULL DEFAULT '0' COMMENT '网关应用ID',
`front_path` varchar(128) NOT NULL DEFAULT '' COMMENT '网关接口',
`back_path` varchar(128) NOT NULL DEFAULT '' COMMENT '后端接口',
`timeout` int unsigned NOT NULL DEFAULT '0' COMMENT '超时时间(单位:ms,0为不限制)',
`max_flow` int unsigned NOT NULL DEFAULT '0' COMMENT '限流阈值(0:不限流)',
`authkeys` varchar(256) NOT NULL DEFAULT '' COMMENT '鉴权策略(多个用,分隔)',
`is_mock` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '是否mock',
`mock` varchar(4096) NOT NULL DEFAULT '' COMMENT 'mock内容',
`remark` varchar(256) NOT NULL DEFAULT '' COMMENT '接口说明',
`status` int unsigned NOT NULL DEFAULT '0' COMMENT '状态(0:离线,1:在线)',
`uid` mediumint(8) unsigned NOT NULL DEFAULT '0' COMMENT '创建用户ID',
`ctime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建日期',
`mtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
`isdel` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
PRIMARY KEY (`id`),
UNIQUE KEY `uk_app_id_front_path` (`app_id`,`front_path`),
KEY `idx_app_id_status` (`app_id`,`status`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='网关接口表';

--
-- 表数据
--
INSERT IGNORE INTO `gw_api` (`id`,`app_id`,`front_path`,`back_path`) VALUES
(1,'1','/ping','/ping');

--
-- 表结构: gw_api_auth
--
CREATE TABLE IF NOT EXISTS `gw_api_auth` (
`id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
`authkey` varchar(64) NOT NULL DEFAULT '' COMMENT '鉴权key',
`name` varchar(64) NOT NULL DEFAULT '' COMMENT '名称',
`remark` varchar(256) NOT NULL DEFAULT '' COMMENT '备注',
`isdel` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标志',
PRIMARY KEY (`id`),
UNIQUE KEY `uk_authkey` (`authkey`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='API鉴权配置表';

--
-- 表数据
--
INSERT IGNORE INTO `gw_api_auth` (`id`,`authkey`,`name`,`remark`) VALUES
(1,'ticket','ticket认证','登录鉴权');


