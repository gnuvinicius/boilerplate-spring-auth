#!/bin/bash
# mvn versions:set -DnewVersion=staging
mvn clean package

docker build --no-cache \
  --buid-arg JWT_SECRET=pdkBtv9driN4lSGTNeOfBK6p5IC6iz \
  --build-arg APP_NAME=./target/notes-api-0.0.1-SNAPSHOT \
  --build-arg POSTGRES_HOST=192.168.0.220 \
  --build-arg POSTGRES_PASSWD=2AkByM4NfHFkeJz --tag notes-api:staging .
            
docker rm notes-api -f
docker run -d --name notes-api --network grg-net notes-api:staging