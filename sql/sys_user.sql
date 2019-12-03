CREATE TABLE `sys_user` (
  `user_id` varchar(11) NOT NULL COMMENT '用户id',
  `user_name` varchar(64) NOT NULL COMMENT '用户名',
  `password` varchar(64) NOT NULL COMMENT '用户密码',
  `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(16) DEFAULT NULL COMMENT '手机号码',
  `sex` char(1) DEFAULT NULL COMMENT '性别',
  `birthday` datetime DEFAULT NULL COMMENT '生日',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `remark` varchar(128) DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';