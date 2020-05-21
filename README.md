## 지도 마커 클러스터링 (안드로이드) 

2020 NAVER CAMPUS HACKDAY - 네이버 지도 이미지 마커 클러스터링

- 공통 이슈: https://github.com/2020-NAVER-CAMPUS-HACKDAY/ImageClusteringForMap
- iOS: https://github.com/2020-NAVER-CAMPUS-HACKDAY/iOS_map_clustering

### 프로젝트 개요

네이버 지도에서 클러스터링이 적용된 음식점 정보를 제공하는 프로젝트입니다.

### 주요 기능

1. 음식점 지도 : 네이버 지도에 클러스터링이 적용된 결과를 확인할 수 있는 지도 앱입니다. 카테고리에 따라 음식점을 필터링하거나, 음식점의 간단한 정보를 조회할 수 있습니다.
2. 클러스터링 : 좁은 영역에 많은 수의 마커가 있을 때, 서로 가까이 있는 마커를 그룹화합니다. 클러스터링 주요 로직은 크게 `알고리즘`과 `렌더링`으로 구분됩니다.
   - 알고리즘 : 클러스터의 생성 위치와, 어떤 마커를 클러스터에 넣을지를 결정합니다. 알고리즘이 동작하는 flow는 [wiki]([https://github.com/2020-NAVER-CAMPUS-HACKDAY/android_map_clustering/wiki/%ED%81%B4%EB%9F%AC%EC%8A%A4%ED%84%B0%EB%A7%81-%EC%95%8C%EA%B3%A0%EB%A6%AC%EC%A6%98-%EB%8F%99%EC%9E%91-%EA%B3%BC%EC%A0%95](https://github.com/2020-NAVER-CAMPUS-HACKDAY/android_map_clustering/wiki/클러스터링-알고리즘-동작-과정))에서 확인 가능합니다.
   - 렌더링 : 알고리즘의 연산 결과를 사용자가 확인할 수 있도록 시각화합니다.

3. asset 파일 파싱 : 로컬 환경에서 json format의 asset 파일을 읽고 파싱합니다.

### 개발 결과물

|                            클러스터링 적용 전 |                           클러스터링 적용 후                           |
| :----------------------------------------------------------: | :----------------------------------------------------------: |
| <img src="https://github.com/2020-NAVER-CAMPUS-HACKDAY/android_map_clustering/blob/master/screenshot/screenshot1.png" width="300" /> | <img src="https://github.com/2020-NAVER-CAMPUS-HACKDAY/android_map_clustering/blob/master/screenshot/screenshot2.jpeg" width="270" /> |

### 개발환경

- Android Studio `3.6.3`
- Kotlin `1.3.72`

### 라이브러리

- [Naver Map SDK](https://navermaps.github.io/android-map-sdk/guide-ko/1.html)
- [glide](https://github.com/bumptech/glide)
- [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization)
- [AAC](https://developer.android.com/topic/libraries/architecture)
- [databinding](https://developer.android.com/topic/libraries/data-binding?hl=ko)
- [material-components-android](https://github.com/material-components/material-components-android)

### 시작하기

프로젝트 루트 디렉토리에 `apikey.properties` 파일을 생성한 후, 아래의 두줄을 추가합니다.

API Key는 [네이버 지도 안드로이드 SDK 시작하기](https://navermaps.github.io/android-map-sdk/guide-ko/1.html) 가이드를 참고해서 발급 받을 수 있습니다.

```bash
NAVER_CLIENT_ID="발급 받은 Client key"
NAVER_CLIENT_SECRET="발급 받은 Secret key"
```
