
# moRoom (모두의 원룸)
<b>대학가 원룸 거주 후기 어플</br>

<p>
<img width="50%" src="https://github.com/Ji4017/moRoom-Android/assets/90889656/917a27b5-9811-4173-ae97-d919208eabe3">
</p>

# Contents
- <b> <a href="#1"> 🔗 Motive </a> </b>
- <b> <a href="#2"> 🔗 App Characteristic </a> </b>
- <b> <a href="#3"> 🔗 Project Characteristic </a> </b>
- <b> <a href="#4"> 🔗 Technology </a> </b>
- <b> <a href="#5"> 🔗 Result </a> </b>
- <b> <a href="#6"> 🔗 PlayStoreLink </a> </b>

<hr>

<h2 id="1">
    <b>Motive</b>
</h2>

[공급자 위주의 시스템인 임대차 시장의 정보 비대칭 해소]

현재 많은 사람이 집을 자산으로 바라봅니다.  
그 결과 기존 부동산 플랫폼들도 투자의 관점에서 정보를 제공합니다.  
그러나 '이 집이 얼마나 살기 좋은가'에 대한 거주 정보를 제공하는 플랫폼은 없습니다.

소비자는 임대인이 제공하는 정보에 의존해 집을 구해야만 했고  
부동산 플랫폼들 역시 중개사가 입력한 정보만을 보여주기에 감춰진 정보를 알 수 없었습니다.

이처럼 원룸의 임대차 시장이 공급자 위주의 시스템이라는 것에 문제의식을 느끼고 이러한 경험 비대칭 문제에 집중했습니다.

어플에서는 층간소음, 벌레, 하자, 관리비부터 임대인이 보증금은 바로 주는지 등의 '경험 데이터'를 확인할 수 있습니다. 공인중개소에서도 말해주지 않는 정보입니다.

<hr>

<h2 id="2">
    <b>App Characteristic</b>
</h2>

- 학교 이메일 인증을 받은 유저만 회원가입 및 후기작성 가능
- 후기 작성시 층수 정보를 입력함으로 디테일한 후기 제공
- 수기대신 사용자 경험을 고려한 리스트 체크방식의 편리한 후기작성
- 후기를 작성하지 않은 유저에게는 후기열람 제한으로 후기작성 유도

<hr>

<h2 id="3">
    <b>Project Characteristic</b>
</h2>

- 게시글 작성자를 랜덤한 값인 UID로 처리해 익명으로 후기 작성
- 동적링크를 이용한 이메일 인증 및 회원가입
- 지도의 Marker를 이용한 해당 주소의 후기로 이동
- Android Location Permission Handling
- 동적UI (Firebase + RecyclerView + ViewModel + LiveData + DataBinding + BindingAdapter)
- Open Source - Sticky Header를 적용한 주소 검색창

<hr>

<h2 id="4">
    <b>Technology</b>
</h2>

### TechStack
`Java` `Kotlin` `ViewModel` `LiveData` `DataBinding` `Navigation` `Daum Postcode API` `Kakao Maps API` `Figma`  
`Firebase` (Authentication, DynamicLinks, RealtimeDatabase, Hosting)

### Architecture
`MVVM`

<hr>

<h2 id="5">
    <b>Result</b>
</h2>

<h3 align="left">홈 화면</h3>

<p>
<img width="25%" src="https://github.com/Ji4017/moRoom-Android/assets/90889656/ad886007-ce32-4730-bf1b-4bff4eaffdf7">
<img width="25%" src="https://github.com/Ji4017/moRoom-Android/assets/90889656/5e92bb7d-0015-4fb9-9283-7a10a3afedeb">
</p>
<br>

<h3 align="left">지도</h3>
<p>
<img width="25%" src="https://github.com/Ji4017/moRoom-Android/assets/90889656/1f711475-a281-4339-bbb4-30fa292d2181">
<img width="25%" src="https://github.com/Ji4017/moRoom-Android/assets/90889656/62a6d37b-4a5e-46cf-b5cd-a9259588f992">
</p>
<br>

<h3 align="left">검색</h3>
<p>
<img width="25%" src="https://github.com/Ji4017/moRoom-Android/assets/90889656/d503bb86-e7be-49db-92f3-80620f6b87e2">
<img width="25%" src="https://github.com/Ji4017/moRoom-Android/assets/90889656/cdf1215b-bc91-4696-8151-3f4a0e929ebc">
</p>
<br>

<h3 align="left">후기 작성</h3>
<p>
<img width="25%" src="https://github.com/Ji4017/moRoom-Android/assets/90889656/14c2dd5b-3fa1-48a3-94bf-e7349cf4bbae">
</p>
<br>

<h3 align="left">로그인</h3>
<p>
<img width="25%" src="https://github.com/Ji4017/moRoom-Android/assets/90889656/d1a99bd1-4110-4c3d-bbb6-70a96200ca70">
<img width="25%" src="https://github.com/Ji4017/moRoom-Android/assets/90889656/f9aab642-6a01-42c8-be63-6e64ce3d6994">
</p>
<br>

<h3 align="left">회원가입</h3>
<p>
<img width="25%" src="https://github.com/Ji4017/moRoom-Android/assets/90889656/3ec00b17-ca62-43ee-84c3-589f0a331dad">
<img width="25%" src="https://github.com/Ji4017/moRoom-Android/assets/90889656/49b3b3e0-96a3-4cda-b1fa-63e36b1b3d40">
<img width="25%" src="https://github.com/Ji4017/moRoom-Android/assets/90889656/f5e3ec93-b736-4903-ad51-7f46e82b1a34">
</p>

<hr>

<h2 id="6">
    <b>PlayStoreLink</b>
</h2>
<a href="https://play.google.com/store/apps/details?id=com.moroom.android&hl=ko-KR" target="_blank">플레이스토어</a>
