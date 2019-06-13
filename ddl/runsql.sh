#!/bin/bash
#
# 执行SQL
#
mysqlbin='mysql -h127.0.0.1 -P3306 -uroot -proot'
sqlfile='gateway.sql'

$mysqlbin < $sqlfile

echo "success";
