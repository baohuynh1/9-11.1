#!/usr/bin/env sh
# Build JAR cho 4 service (tuần tự, tiết kiệm RAM). Dùng BUILD_ROOT trong .env nếu có.
set -e
cd "$(dirname "$0")"
ROOT=$(grep -E '^BUILD_ROOT=' .env 2>/dev/null | cut -d= -f2-)
ROOT=${ROOT:-../exercise_01}
for s in user-service restaurant-service order-service notification-service; do
  echo "==== $s ===="
  (cd "$ROOT/$s" && chmod +x gradlew && ./gradlew bootJar)
done
