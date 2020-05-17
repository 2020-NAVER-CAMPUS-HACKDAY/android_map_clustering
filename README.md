## 지도 마커 클러스터링 (안드로이드) 
2020 NAVER CAMPUS HACKDAY - 네이버 지도 이미지 마커 클러스터링
- 공통 이슈: https://github.com/2020-NAVER-CAMPUS-HACKDAY/ImageClusteringForMap
- iOS: https://github.com/2020-NAVER-CAMPUS-HACKDAY/iOS_map_clustering

### 개발환경
- Android Studio `3.6.3`
- Kotlin `1.3.72`

### 구성

프로젝트 루트 디렉토리에 `apikey.properties` 파일을 아래와 같이 만듭니다.

```bash
NAVER_CLIENT_ID="발급 받은 Client key"
NAVER_CLIENT_SECRET="발급 받은 Secret key"
```

API key를 얻으려면 [네이버 지도 안드로이드 SDK 시작하기](https://navermaps.github.io/android-map-sdk/guide-ko/1.html) 가이드를 참고해서 발급 받을 수 있습니다.
