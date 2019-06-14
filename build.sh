#!/bin/bash

target="./target"
outdir="./output"

# mvn打包
mvn clean install package -Dmaven.test.skip=true -U -e -Ponline

##########################################
# 打包成可执行的环境
##########################################
if [ -d $outdir ]; then
    rm -rf $outdir
fi
mkdir -p $outdir

function cpfiles()
{
    for i in $@; do
        cp -r $i $outdir
    done
}

# 拷贝文件
cpfiles $target/*.jar
cpfiles profile/online/*
cpfiles control.sh

echo "!!!BUILD SUCCESS!!!"
