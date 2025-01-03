# Backend v2

## Swagger 
`/back/api/swagger-ui/index.html`

## 실행 방법
### 1. docker-compose.yml로 컨테이너 실행 
### 2. Environment Variables 설정
1. 공통  
AWS_IAM_ACCESSKEY=;  
AWS_IAM_SECRETKEY=;  
GOOGLE_CLIENT_ID=;  
GOOGLE_CLIENT_SECRET=;  
KAKAO_CLIENT_SECRET=;  

2. 로컬  
1) -Dspring.profiles.active=local 설정  
2)   
LOCAL_GOOGLE_REDIRECT_URL=http://localhost:3000;  
LOCAL_KAKAO_REDIRECT_URL=http://localhost:3000;  
LOCAL_DATABASE_URL=jdbc:mysql://localhost:3306/vvue;  
LOCAL_DATABASE_USERNAME=root;  
LOCAL_DATABASE_PASSWORD=;  
LOCAL_JWT_SECRET=;  
LOCAL_REDIS_HOST=localhost;  
LOCAL_REDIS_PASSWORD=root;  
LOCAL_REDIS_PORT=6379;  

3. 배포     
PROD_DATABASE_PASSWORD=;  
PROD_DATABASE_URL=;  
PROD_DATABASE_USERNAME=;  
PROD_JWT_SECRET=;  
PROD_REDIS_HOST=;  
PROD_REDIS_PASSWORD=;  
PROD_REDIS_PORT=;  

### 3. 실행  
