#!/usr/bin/env sh
# Build JAR + Docker image cho cả 4 service (chạy tuần tự để tiết kiệm RAM)
set -e
for s in user-service restaurant-service order-service notification-service; do
  echo "==== $s ===="
  (cd "$s" && ./gradlew bootJar && docker build -t "quickbite-$s:latest" .)
done
docker images | grep quickbite
