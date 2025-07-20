## 부하테스트 (with artillery)

### 설치

```
npm install -g artillery@latest
```

### 버전 확인

```
artillery --version
```

### 테스트 설정 파일 작성

```text
test-config.yml 확인
```

### 테스트 진행

```
artillery run TEST_FILE.yaml --record --key YOUR_API_KEY
```

TEST_FILE : 테스트 시나리오 설정해둔 파일
YOUT_API_KEY : Edit Profile > API Keys 에서 발급

### 테스트  분석

https://www.artillery.io/


