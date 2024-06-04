用户表

```sql
CREATE TABLE `user` (
    `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
    `name` varchar(128) DEFAULT NULL COMMENT '昵称',
    `pwd` varchar(124) DEFAULT NULL COMMENT '密码',
    `head_img` varchar(524) DEFAULT NULL COMMENT '头像',
    `sex` tinyint(2) DEFAULT '1' COMMENT '0表示女，1表示男',
    `points` int(10) DEFAULT '0' COMMENT '积分',
    `mail` varchar(64) DEFAULT NULL COMMENT '邮箱',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    creater     bigint                             null comment '创建者id',
    updater     bigint   default 0                 null comment '更新者id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `mail_idx` (`mail`)
  ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8,comment '用户表';
```

