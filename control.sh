#!/bin/bash
#export JAVA_HOME=/usr/local/jdk1.8.0_65

BASEDIR=$(dirname "$0")
cd $BASEDIR

JAR_FILE=`ls | grep .jar$`
JVM_OPTS=" -Xms3g -Xmx3g -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+UseCMSInitiatingOccupancyOnly -Xloggc:log/gc.log -XX:+PrintGCDetails"
JAVA_BIN="${JAVA_HOME}/bin/java"
PID_FILE="app.pid"
pid=0;


# 启动服务
start(){
    checkPid
    if [ "${pid}"x != "0"x ];then
        echo "please do not repeat "
        return 1
    fi
	echo "start..."
    nohup ${JAVA_BIN} ${JVM_OPTS} -jar ${JAR_FILE} >/dev/null 2>&1 &
    checkPid
    sleep 1s
    return $?
}

# 关闭服务
stop() {
	checkPid
	if [ "${pid}"x != "0"x ];then
		kill -9 "${pid}"
		rm -fr ${PID_FILE}
	fi
	echo "server is stopped."
}

# 重启服务
reload() {
	stop
	start
	exit $?
}

# 检查进程,并将pid写入文件
checkPid(){
    v_pid=$(pgrep -f "${JAR_FILE}" |head -1)
    if [ "${v_pid}"x = x ];then
        echo "java process is shutdown"
        pid=0
    else
        echo "java process is running [pid:${v_pid}]"
        pid=$((v_pid))
    fi
	echo "${v_pid}"> $PID_FILE
}


case $1 in
	start)
      start
      ;;
	stop)
      stop
      ;;
	reload)
      reload
      ;;
	healthCheck)
      healthCheck
      ;;
    *)
echo "arg: start|stop|reload"
esac
exit $?



#echo $BASEDIR
#echo $JVM_OPTS $JAR_FILE $JAVA_ARGS
