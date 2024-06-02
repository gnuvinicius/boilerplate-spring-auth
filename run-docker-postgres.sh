#!/bin/bash

docker run -d --name postgres -p 5432:5432 \
 --restart always -e POSTGRES_PASSWORD=2AkByM4NfHFkeJz \
 -v ./pgdata:/var/lib/postgresql/data postgres:16.3-alpine3.20