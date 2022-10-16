@echo off

rem set up environment for Java
rem set JAVA_HOME=D:\java\jdk1.8.0_40
rem set PATH=%JAVA_HOME%\bin;%JAVA_HOME%\jre\bin;
rem set CLASSPATH=.;%JAVA_HOME%\lib;%JAVA_HOME%\lib\dt.jar;%JAVA_HOME%\lib\tools.jar

CHCP 65001
cd ../
echo starting up ...
set CURRENT_DIR=%cd%
echo %CURRENT_DIR%

set SERVER_OPTS=-Xms1024m -Xmx4096m -Xss1m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=256m

echo %SERVER_OPTS%
java %SERVER_OPTS% -Dfile.encoding=UTF-8 -Djava.ext.dirs="%JAVA_HOME%"\jre\lib\ext;./lib com.vv.java.demo.kafka.consumer.business.KafkaConsumerDemo