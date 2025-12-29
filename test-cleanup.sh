#!/bin/bash

set -e

echo "🧹 테스트 환경 정리 중..."

# Docker Compose 종료 및 볼륨 삭제
docker-compose -f docker-compose.test.yml down -v

# 이미지 정리 (선택사항)
# docker rmi vvue-be-refactoring-app-test 2>/dev/null || true

echo "✅ 테스트 환경이 정리되었습니다!"