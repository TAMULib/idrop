#!/bin/bash

rm -rf /data/grails-core
echo 'removed grails'

rm -rf /data/idrop
echo 'removed iDrop'

rm -rf /etc/idrop-web
echo 'removed idrop-web'

rm -rf /tmp/uploadr
echo 'removed uploadr'

rm -rf /data/tomcat/webapps/idrop-web2/*
echo 'undeployed idrop-wed2'

chef-client

tail -f /data/tomcat/logs/catalina.out
