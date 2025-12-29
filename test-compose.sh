#!/bin/bash

set -e

echo "🏗️  격리된 테스트 환경 시작..."

# 1. 애플리케이션 빌드
echo "📦 애플리케이션 빌드 중..."
./gradlew clean build -x test

# 2. Docker Compose 실행
echo "🐳 Docker 컨테이너 시작 중..."
docker-compose -f docker-compose.test.yml up -d mysql-test redis-test localstack

# 3. 의존성 서비스 헬스체크 대기
echo "⏳ 서비스 준비 대기 중..."
sleep 10

# 4. 애플리케이션 시작
echo "🚀 애플리케이션 시작 중..."
docker-compose -f docker-compose.test.yml up -d app-test

# 5. 애플리케이션 헬스체크 대기
echo "🔍 애플리케이션 헬스체크 중..."
max_retries=30
count=0
until [ $count -ge $max_retries ]
do
  if curl -f http://localhost:8080/back/api/actuator/health &> /dev/null; then
    echo "✅ 애플리케이션이 정상적으로 시작되었습니다!"
    break
  fi
  count=$((count+1))
  echo "대기 중... ($count/$max_retries)"
  sleep 2
done

if [ $count -ge $max_retries ]; then
  echo "❌ 애플리케이션 시작 실패"
  docker-compose -f docker-compose.test.yml logs app-test
  exit 1
fi

echo ""
echo "=========================================="
echo "✅ 테스트 환경이 준비되었습니다!"
echo "=========================================="
echo ""
echo "📍 접속 정보:"
echo "  - API: http://localhost:8080/back/api"
echo "  - Swagger: http://localhost:8080/back/api/swagger-ui/index.html"
echo "  - Actuator: http://localhost:8080/back/api/actuator"
echo "  - MySQL: localhost:3307 (vvue_test/root/test)"
echo "  - Redis: localhost:6380 (testpassword)"
echo "  - LocalStack: http://localhost:4566"
echo ""
echo "📝 유용한 명령어:"
echo "  - 로그 확인: docker-compose -f docker-compose.test.yml logs -f app-test"
echo "  - 종료: docker-compose -f docker-compose.test.yml down"
echo "  - 재시작: docker-compose -f docker-compose.test.yml restart app-test"
echo ""