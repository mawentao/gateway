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
cp -r $target/*.jar $outdir
cp -r profile/online/* $outdir

cp -r src/main/resources/dist/start.sh $outdir



echo "!!!BUILD SUCCESS!!!"
