
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
