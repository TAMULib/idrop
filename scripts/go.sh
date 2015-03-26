#!/bin/bash

service tomcat stop
echo 'stopped tomcat'

service nginx stop
echo 'stopped nginx'

rm -rf /data/tomcat/logs/catalina.out
echo 'removed catalina log file'

rm -rf /data/tomcat/webapps/idrop-web2/*
echo 'undeployed idrop-web2'

cd /data/idrop/idrop-web
grails war idrop-web2.war
echo 'built idrop-web2.war'

cp idrop-web2.war /data/tomcat/webapps/idrop-web2/
cd /data/tomcat/webapps/idrop-web2/
jar -xvf idrop-web2.war
echo 'deployed idrop-web2'

service nginx start
echo 'started nginx'

service tomcat start
echo 'started tomcat'

tail -f /data/tomcat/logs/catalina.out
