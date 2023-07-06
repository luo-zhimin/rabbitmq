# rabbitmq
mq rabbit-mq

## 1. 安装
### 1.1 安装erlang
```
erlang rpm 下载地址 http://packages.erlang-solutions.com/erlang/rpm/centos/7/x86_64

源码下载 https://www.erlang.org/downloads

源码下载进入目录直接编译 ./configure && make && make install
```
### 1.2 安装rabbitmq
```
rpm 下载地址 https://www.rabbitmq.com/install-rpm.html
安装 rpm -ivh xxx.rpm
```

## 2. 启动
```
添加开机启动 RabbitMQ 服务
chkconfig rabbitmq-server on
启动服务
/sbin/service rabbitmq-server start 
查看服务状态
/sbin/service rabbitmq-server status

开启 web 管理插件
rabbitmq-plugins enable rabbitmq_management

web 插件地址
http://ip:15672

创建用户
[//]: # (rabbitmqctl add_user 用户名 密码)
rabbitmqctl add_user admin admin

设置用户角色
rabbitmqctl set_user_tags admin administrator

设置用户权限
set_permissions [-p <vhostpath>] <user> <conf> <write> <read>
rabbitmqctl set_permissions -p "/" admin ".*" ".*" ".*"
用户 user_admin 具有/vhost1 这个 virtual host 中所有资源的配置、写、读权限

当前用户和角色
rabbitmqctl list_users
```
