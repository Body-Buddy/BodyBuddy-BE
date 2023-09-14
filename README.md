# 1. what is *BoddyBuddy* ?
### 🥲운동을 시작하며 기구 사용법이나 운동 순서에 대한 고민으로 막막했던 적 있지 않으신가요?
### 😍혼자 운동하는 초보자 분들에게 같은 헬스장 회원들과 '운동 친구'를 매칭해주는 앱,

## 💪🏻 ***바디버디*** 를 소개합니다 !

---
# 2. 팀 소개
|                      👑리더                      |                     🧚‍♀️부리더                      |                          👨‍💻팀원                          |                         👨‍💻팀원                          |                        👨‍💻팀원                        |
|:----------------------------------------------:|:-----------------------------------------------------:|:----------------------------------------------------:|:---------------------------------------------------:|:------------------------------------------------:|
|                      김은비                       |                          심지연                          |                         정민재                          |                         박경환                         |                        이성수                       |
| [Github](https://github.com/eunb1)<br>[Blog]() | [Github](https://github.com/SIMJIYEON93)<br>[Blog]( ) | [Github](https://github.com/hohominjae)<br>[Blog]( ) | [Github](https://github.com/endrmseha)<br>[Blog]( ) | [Github](https://github.com/lss6181)<br>[Blog](https://velog.io/@lss6181) |
<br>
<details>
<summary> <img src="https://img.shields.io/badge/Git Convention-F05032?style=flat&logo=git&logoColor=white"/></summary>

- ### `main` 브랜치에서 직접 `commit` 또는 `push` 하지 않습니다.
- ### `Pull Request`는 2명 이상의 `Approve` 가 있을 때 머지 가능합니다.
- ### ❗️`PR`을 작성하기 전 본인이 작성한 코드가 올바르게 작동하는지 반드시 확인해주세요 ❗️
- ### git `branch` 네이밍 규칙
  - #### `main` 브랜치 
    - 사용자에게 배포 가능한 상태만을 관리합니다.
  - #### `develop` 브랜치 (Default 브랜치) 
    - 기능 개발을 위한 브랜치들을 병합하기 위해 사용합니다. 
    - 모든 기능이 추가되고 버그가 수정되어 배포 가능한 안정적인 상태라면 `develop` 브랜치를 `main` 브랜치에 병합합니다.
  - #### `feature` 브랜치
    - 새로운 기능 개발 및 버그 수정이 필요할 때마다 develop 브랜치로부터 분기합니다. 개발이 완료되면 develop 브랜치에 병합하여 다른 사람들과 공유합니다.
    - `feature/{이슈 번호}-{기능 요약}` 형식으로 브랜치 이름을 작성합니다.
    - 기능요약 부분을 작성할때 띄어쓰기는 `-`를 이용하여 작성합니다. 예시) `feature/3-user-login`
- ### Commit Message 규칙
  - `태그: 제목`의 형태이며, `:`뒤에만 `space`가 있음에 유의합니다.
    ```jsx
    feat: 새로운 기능 추가
    fix: 버그 수정
    docs: 문서 수정
    style: 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우
    refactor: 코드 리팩토링
    test: 테스트 코드, 리팩토링 테스트 코드 추가
    chore: 빌드 업무 수정, 패키지 매니저 수정

    예시) feat: 회원 가입 기능 구현
    ```
</details>

<details>
<summary> <img src="https://img.shields.io/badge/Coding Convention-C71D23?style=flat&logo=&logoColor=white"/></summary>

- ### [네이버 캠퍼스 핵데이 Java 코딩 컨벤션](https://naver.github.io/hackday-conventions-java/)을 준수합니다.
- ### 가독성을 높이기 위해 `코드 포맷팅`은 습관처럼 해주세요!
  - `Intellij` 단축키로 자동 정렬이 가능합니다.
  - 윈도우 `Ctrl+Alt+L` / 맥 `Cmd+Alt+L`
  - `Tab size : 2`, `Indent : 4`로 설정합니다.
</details>

---

# 3. 주요 기능

## 🏋️‍♀ *나의 헬스장 등록*
- ### 사용자는 자신의 헬스장을 등록하여 같은 헬스장의 회원들과 소통할 수 있습니다.

## 👥 *운동 친구 매칭*
- ### 🔎 *친구 찾기* : 사용자 `프로필`을 바탕으로 최적의 운동 친구를 `제안`합니다.
- ### 💬 *1:1 채팅* : 매칭된 회원과 `개별 대화`가 가능합니다.
- ### ❌ *친구 차단* : 원치 않는 사용자와의 연락을 `차단`합니다.

## 📢 *실시간 알림 기능*
- ### 새로운 채팅 및 중요 알림을 사용자에게 즉시 전달합니다.

## 📝 *커뮤니티 게시판*
- ### `💡운동 Tip`, `📰식단 정보`, `💪동기 부여`, `🦾기구 추천` 등의 다양한 주제로 <br><br>`게시글`과 `댓글`을 작성하고 공유할 수 있습니다.
---

# 4. 기술 스택
## Frontend

- **Framework**: <img src="https://img.shields.io/badge/Vue.js-4FC08D?style=flat&logo=vue.js&logoColor=white"> +
  <img src="https://img.shields.io/badge/HTML-E34F26?style=flat&logo=html5&logoColor=white">
  <br><br>
- **UI Toolkit**: <img src="https://img.shields.io/badge/Tailwind CSS-06B6D4?style=flat&logo=tailwindcss&logoColor=white"> +
  <img src="https://img.shields.io/badge/CSS-1572B6?style=flat&logo=css3&logoColor=white">
  <br><br>
- **Build & Dev Environment**: <img src="https://img.shields.io/badge/Vite-646CFF?style=flat&logo=vite&logoColor=white">
  <br><br>
- **Deployment**: <img src="https://img.shields.io/badge/Vercel-000000?style=flat&logo=vercel&logoColor=white">


## Backend

- **Framework**: <img src="https://img.shields.io/badge/Spring_Boot 3.1.2-6DB33F?style=flat&logo=springboot&logoColor=white"/>
  <br><br>
- **Programming Language**: <img src="https://img.shields.io/badge/Java 17-007396?style=flat"/>
  <br><br>
- **Database**: <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=mysql&logoColor=white"/>
<img src="https://img.shields.io/badge/Redis-DC382D?style=flat&logo=redis&logoColor=white"/>
<img src="https://img.shields.io/badge/AWS RDS-527FFF?style=flat&logo=amazonrds&logoColor=white"/>
  <br><br>
- **CI / CD**: <img src="https://img.shields.io/badge/GitHub Actions-2088FF?style=flat&logo=githubactions&logoColor=white"/>
  <img src="https://img.shields.io/badge/Amazon_EC2-FF9900?style=flat&logo=amazonec2&logoColor=white"/>
  <img src="https://img.shields.io/badge/Amazon_S3-569A31?style=flat&logo=amazons3&logoColor=white"/>

## +@
<img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=flat&logo=springsecurity&logoColor=white"/>
<img src="https://img.shields.io/badge/Spring_Data_JPA-6DB33F?style=flat">
<img src="https://img.shields.io/badge/WebSocket-CD9834?style=flat"/>
<img src="https://img.shields.io/badge/STOMP-E6E6E6?style=flat&logo=&logoColor=white"/><br>
<img src="https://img.shields.io/badge/JWT-A9225C?style=flat">
<img src="https://img.shields.io/badge/Gradle-02303A?style=flat&logo=gradle&logoColor=white"/>
<img src="https://img.shields.io/badge/Hibernate-59666C?style=flat&logo=Hibernate&logoColor=white"/>
<img src="https://img.shields.io/badge/Postman-FF6C37?style=flat&logo=Postman&logoColor=white"/>


---

# 5. 시연영상, 발표자료
### 📽️[발표영상](https://www.youtube.com/watch?v=csJrEozcYqE)
### 📜[발표자료](https://www.notion.so/bodybuddy3/881c232533fb431a839652a9fae5670a?pvs=4)

---

# 6. 서비스 아키텍쳐

---
# 7. 기술적 의사결정

<details>
<summary>Spring Data JPA</summary>

### 💡 데이터 액세스 레이어 구축을 위한 주요 고려 사항은 **코드의 간결성**과 **유지보수성** 이었습니다.
- #### 👍 **코드의 간결성**
  - `Repository interface`를 통해 복잡한 `query`도 직관적으로 표현할 수 있습니다.
- #### 👍 **`Query` 최적화**
  - `JPA`가 쿼리의 성능을 내부적으로 **최적화** 합니다.
- #### 👍 **코드 `중복` 최소화**
  - `기본 CRUD 메소드를 제공`하여 반복되는 코드의 작성을 줄입니다.
- #### 👍 **데이터베이스 `중립성`**
  - 다양한 데이터베이스 전환 시 구현 로직 변경 없이 호환성을 유지합니다.
</details>

<details>
<summary>Redis</summary>

### 💡 **JWT** 를 이용한 인증에서 토큰의 유효성과 관리 문제를 해결하기 위해 `Redis`를 선택했습니다.
- #### 👍 **빠른 응답 시간**
  - `인메모리` 특성으로 **빠른 데이터 액세스**가 가능합니다.
- #### 👍 **`휘발성` 데이터 관리**
  - `TTL` 기능을 이용해 `토큰의 생명 주기`를 효율적으로 관리합니다.
- #### 👍 **분산 환경 지원**
  - `대규모` 사용자 환경에서도 `안정적인` 성능을 보장합니다.
- #### 👍 **`Token Blacklisting`**
  - 필요한 토큰의 `접근을 제한`하는 기능을 구현합니다.
- #### 👍 **확장성**
  - **세션 관리**부터 **실시간 알림**까지 `다양한 활용 가능성`이 있습니다.
</details>

<details>
<summary>Spring Security & OAuth2</summary>

### 💡 사용자의 로그인 경험을 개선하기 위해 소셜 로그인 기능을 도입하고자 하였습니다.
### ➡️Google, Naver, Kakao와의 연동을 위해 Spring Security와 OAuth2를 활용했습니다.
- #### 👍 **인증 서버 연동**
  - Spring Security가 OAuth2 클라이언트 역할을 하여 소셜 플 랫폼의 인증폼서서버 신합니다.
- #### 👍 **사용자 정보 수집**
  - 인증 후 필요한 사용자의 기본 정보를 수집하여 우리 서비스의 DB에 저장하거나 업데이트합니다.
- #### 👍 **보안**
  - 인증 토큰 및 사용자 정보는 안전하게 보호됩니다.
</details>

<details>
<summary>WebSocket & STOMP</summary>

### 💡 WebSocket
- 헬스 친구 매칭 서비스에서 `실시간 채팅`은 중요한 기능 중 하나로, 사용자들에게 빠른 응답 및 `실시간 소통의 필요성`이 있었습니다.<br>
기존의 `HTTP 요청-응답 모델`에서는 이러한 실시간 통신을 구현하기 힘들다는 `문제점`이 있었고,<br>
이를 해결하기 위해 `실시간 양방향 통신`을 지원하는 프로토콜인 `WebSocket`을 도입하였습니다.<br>
`서버와 클라이언트` 간의 `지속적인 연결 상태를 유지`하게 해 주어 실시간 데이터 교환이 가능합니다.

### 💡 STOMP (Simple Text Oriented Messaging Protocol)
- 단순히 웹소켓만으로는 메시지 라우팅이나 메시지 형식 정의 등의 복잡한 기능을 처리하기 어려웠습니다.<br>
  채팅에서는 여러 채팅방이나 다양한 상황에 따른 메시지 전송이 필요하기 때문에
  STOMP를 사용하여 이를 효율적으로 관리하고 구현했습니다.<br>
  STOMP는 간단한 `텍스트 기반 메시징 프로토콜`로, `WebSocket 위에서 동작`하며, `publish/subscribe`, `point-to-point` 등의 메시징 패턴을 지원합니다.
</details>

<details>
<summary>Server-Sent Events (SSE)</summary>

### 💡 채팅 메시지, 새로운 게시글 및 댓글에 대한 실시간 알림을 구현하고자 하였습니다.
### ➡️ 서버에서 클라이언트로의 단방향 정보 전달에 특화된 SSE를 선택하였습니다.
- #### 👍 **단방향 통신**
  - SSE는 서버에서 클라이언트로의 단방향 통신이 가능합니다. 이는 알림 서비스에서 필요한 특징이며, 
  <br>특별한 설정이나 복잡한 핸드쉐이크 없이도 실시간 정보를 전송할 수 있습니다.
- #### 👍 **자동 재연결**
  - 클라이언트와 서버 간의 연결이 끊어진 경우, SSE는 자동으로 재연결을 시도합니다. 
  <br>이로 인해 잠시의 네트워크 문제로 인해 알림이 누락되는 경우를 최소화할 수 있습니다.
- #### 👍 **기본 HTTP 프로토콜 활용**
  - SSE는 표준 HTTP 프로토콜을 사용하기 때문에, 별도의 프로토콜 설정이나 프록시 설정 변경 없이도 기존의 인프라 위에서 운영할 수 있습니다.
- #### 👍 **효율적인 리소스 사용**
  - SSE는 헤더 정보와 함께 간단한 텍스트 형식의 메시지를 전송하기 때문에, 리소스를 효율적으로 사용하면서도 빠른 알림 전달이 가능합니다.
</details>

<details>
<summary>Github Actions</summary>

### 💡 CI/CD 파이프라인을 구축하기 위해 Github Actions를 사용했습니다.
- #### 👍 **통합 환경**
  - 소스 코드 저장소와 CI/CD 환경이 통합되어 있습니다.
- #### 👍 **쉬운 구성**
  - YML 파일 기반으로 파이프라인을 설정하여 복잡한 설정 없이도 CI/CD를 구축할 수 있습니다.
</details>

---

# 8. 트러블 슈팅
<details>
<summary>채팅 기능(STOMP 적용 전 websocketHandler로 구현하던 시점)</summary>

### 소켓 연결을 시도한 클라이언트(사용자)의 session을 HashSet에 담아 관리.
```java
private Set<WebSocketSession> sessions = new HashSet<>();
```
- ### 문제
    해당 필드를 `Dto`쪽에 선언하여 session을 추가해주고 추가된 session들에게 채팅 메세지를 뿌려준다는 의도였지만, `sessions.add`를 했는데 기존에 추가해뒀던 session은 유지되지 않는 문제 발생.
- ### 원인 
    `Dto`는 `WebSocketHandler`에서 로직이 수행되면서 `새로운 객체가 생성`되어 각 클라이언트들의 요청 마다 `다른 Dto`가 생성되어 버리는 셈. 그러므로 그 안에 필드로 있던 `sessions` 또한 유지가 아닌 `요청마다 새로 만들어지는 것`이다.
- ### 해결 
    `Dto`가 아닌 `WebSocketHandler` 클래스에 필드로 선언을 해주어 유지됨을 확인.
- ### 추가 개선 
    필드로 선언 하지않고 `SessionManager`라는 클래스를 따로 만들어 `클래스 주입`을 받음으로서 좀 더 객체화 할 수 있게 되었음.  `Set` 자료구조로 담으니 채팅방 별로 구분하여 메세지를 보내는 처리를 할 수가 없어 `Map`으로 변경.
<br>`(Key : 채팅방Id, Value : 해당채팅방에 들어온 session들 담는 Set자료구조)`
```java
private Map<Long, Set<WebSocketSession>> sessions = new HashMap<>();
```
</details>

<details>
<summary>CORS 설정</summary>

- ### 문제
  CORS 설정을 했음에도 프론트에서 백엔드로 로그인 요청을 보낼 때 에러 발생하는 문제
  <img style="display: block;-webkit-user-select: none;margin: auto;cursor: zoom-in;background-color: hsl(0, 0%, 90%);transition: background-color 300ms;" src="https://ifh.cc/g/f2lBWW.jpg" width="636" height="206">
- ### 원인
  스프링 시큐리티 필터 체인은 웹 MVC 필터 체인보다 먼저 실행되므로 스프링 시큐리티 필터가 요청을 차단하게 되어 웹 MVC의 CORS 설정에 도달하지 못함, 스프링 시큐리티에도 별도의 설정이 필요!
- ### 해결
  `CorsConfigurationSource` 타입 Bean 등록 후 스프링 시큐리티 설정에서 CORS 활성화
```java
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        // 기타 설정 ...
    }
}
```

</details>

<details>
<summary>검색 기능</summary>

- ### 문제
  제목이 일치해야만 검색이 되는 문제 발생
- ### 원인
  제목으로 검색 기능을 레포지토리의 findbyTitle로 구성
```java
List<Post> findByTitle(String postTitle);
```
- ### 해결
    - #### 검색 기능을 개선하면서 제목만 검색이 가능하게 할 것인지 제목, 컨텐츠 검색이 가능하게 할 것이지에 대한 고민이 생겼습니다.<br>제목만 검색이라면 단순 like검색으로 커버할 수 있는 지점이 어느정도 있을 것이고, 그 지점을 넘어 성능상 한계가 왔을 때 어떤 대안으로 발전시킬지 논의하게 되었습니다.
    - #### 대안으로는 `ES(Elastic Search)`가 선택 되었는데 ES는 장단이 확실했습니다.
      - 장점 : Scale out을 통한 지속적인 확장 가능
      - 단점 : 비용의 발생

</details>

---
# 9. ERD 설계도
<img src='https://ifh.cc/g/0X6pfX.jpg' border='0'>
----
# 10. API 설계도
[API 설계도](https://www.notion.so/bodybuddy3/API-baf2b76c1f6e48d5aeda0ee39957bc32?pvs=4)