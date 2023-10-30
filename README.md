
# moRoom (모두의 원룸)
<b>대학가 부동산 후기 어플</br>

# Contents
- <b> <a href="#1"> 🔗 Motive </a> </b>
- <b> <a href="#2"> 🔗 App Characteristic </a> </b>
- <b> <a href="#3"> 🔗 Project Characteristic </a> </b>
- <b> <a href="#4"> 🔗 Technology </a> </b>
- <b> <a href="#5"> 🔗 Result </a> </b>

<hr>

<h2 id="1">
    <b>Motive</b>
</h2>

빌라왕 사건으로 거주중인 방과 집주인에 대한 후기 정보가 절실하던 중  
기존의 부동산 후기 앱이 있었으나 전 연령층과 모든 부동산 유형을 타겟팅한 만큼  
대학가 부동산의 후기 정보가 상당히 부족했고 대학생들에게 접근성이 부족한 것으로 판단되어  
기존의 중고거래 플랫폼 시장에서 성공한 당근마켓과 같이 사용자 그룹을 재정의해  
해당 학교 학생끼리 사용하고 대학가 원룸만 타겟팅함으로 접근성을 높여  
현재보다 대학가 부동산의 후기 데이터가 많아질 것을 기대하며  
대학생들이 방을 구함에 있어 도움이 되고자 시작함

<hr>

<h2 id="2">
    <b>App Characteristic</b>
</h2>

- 학교 이메일 인증을 받은 유저만 회원가입 및 후기작성 가능
- 후기 작성시 층수 정보를 입력해 디테일한 후기 제공 가능
- 수기대신 사용자 경험을 고려한 리스트 체크방식의 편리한 후기작성
- 후기를 작성하지 않은 유저에게는 후기열람 제한으로 후기작성 유도

<hr>

<h2 id="3">
    <b>Project Characteristic</b>
</h2>

- 게시글 작성자를 랜덤한 값인 UID로 처리해 익명으로 후기 작성,
- 액티비티에서 다른 액티비티의 Fragment로 이동
- 동적링크를 이용한 이메일 인증 및 회원가입
- 지도의 Marker를 이용한 해당 주소의 후기로 이동
- Android Location Permission Handling
- 동적UI (MVC + Firebase + RecyclerView)
- Sticky Header를 적용한 주소 검색창

<hr>

<h2 id="4">
    <b>Technology</b>
</h2>

### TechStack
`Java`, `DataBinding`, `ViewModel`, `Navigation`, `RecyclerView`, `Daum Postcode API`, `Kakao Maps API`, `Firebase` (Authentication, DynamicLinks, RealtimeDatabase, Hosting)

<hr>

<h2 id="5">
    <b>Result</b>
</h2>

<h3 align="left">메인 화면</h3>

<p align="left">
<img width="25%" src="https://github.com/Ji4017/moroom/assets/90889656/167fb99a-5958-49dd-962f-b60f9a823d78">
<img width="25%" src="https://github.com/Ji4017/moroom/assets/90889656/4f093a18-3065-4d63-a36c-d2466ead087a">
</p>
<br>

<h3 align="left">지도</h3>
<p align="left">
<img width="25%" src="https://github.com/Ji4017/moroom/assets/90889656/bd77b7cc-f1a0-4df6-af1f-d3496bea0ffa">
<img width="25%" src="https://github.com/Ji4017/moroom/assets/90889656/1334a117-21f9-4acf-96f3-94ea560952c5">
<img width="25%" src="https://github.com/Ji4017/moroom/assets/90889656/f7fff8b2-6447-4b83-8dfc-f6dce077ae92">
</p>
<br>

<h3 align="left">검색</h3>
<p align="left">
<img width="25%" src="https://github.com/Ji4017/moroom/assets/90889656/641ae3b6-fe56-4bf3-bf27-2d762412fcad">
<img width="25%" src="https://github.com/Ji4017/moroom/assets/90889656/fc3b62ae-eca8-43bb-a634-d1c7f6ed3c4c">
</p>
<br>

<h3 align="left">회원가입</h3>
<p align="left">
<img width="25%" src="https://github.com/Ji4017/moroom/assets/90889656/ada35d73-89c0-47ce-92fc-9fac7bcfef04">
<img width="25%" src="https://github.com/Ji4017/moroom/assets/90889656/ba0774b7-4fea-42c4-82bf-2fc99b4ab714">
<img width="25%" src="https://github.com/Ji4017/moroom/assets/90889656/daa297ca-9a2e-416c-b3f6-755fec7067c1">
<img width="25%" src="https://github.com/Ji4017/moroom/assets/90889656/87e9d5b9-e1e6-4c39-aeae-4c701e688c62">
<img width="25%" src="https://github.com/Ji4017/moroom/assets/90889656/7cc42bd1-977c-4bbe-af7f-8e860234bd63">
</p>

<h3 align="left">후기 작성</h3>
<p align="left">
<img width="25%" src="https://github.com/Ji4017/moroom/assets/90889656/f22f2693-dbfe-43db-8554-6d2567f4f529">
</p>
