#!/bin/sh

rm -f tpid

nohup java -jar happy-chat-backend-0.0.1-SNAPSHOT.jar --spring.config.location=application.properties > /dev/null 2>&1 &

echo $! > tpid

echo Start Success!