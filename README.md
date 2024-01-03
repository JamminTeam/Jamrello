# 😎 Jamrello - 재밌는 잼렐로
<img width="512" alt="스크린샷 2024-01-02 오전 12 14 48" src="https://github.com/JamminTeam/Jamrello/assets/123870616/195e0e84-5225-4ed3-919d-1b9bf1c1aaf6"> <br>
오늘 일은 내일로? 잼렐로와 함께면 바로바로!

# 😎 개발환경
언어 - JDK 17<br>
환경 - SpringBoot 3.2.1<br>
배포 환경 - AWS S3 / EC2 / ELASTICACHE<br>
DB - MySQL 8.1, Redis<br>
<br>

# 😎 시연 영상
[![잼렐로 시연영상](http://img.youtube.com/vi/2JrGnKTE0IU/0.jpg)](https://youtu.be/2JrGnKTE0IU?t=0s)
<br>
# 😎 Links
<a href="https://documenter.getpostman.com/view/30896712/2s9YsDmFos"> PostMan </a> <br>
<a href="https://www.notion.so/10-ee5b850bfa1a44ea8dc9758b10dc957a?pvs=4"> Notion </a> <br>
<a href="https://www.miricanvas.com/v/12rx2ok"> PPT </a>
<br>
# 😎 Role

| Name | Role |
| --- | --- |
| 조원호 | Catalog API |
| 안준우 | User API, Auth API |
| 박상신 | Board API |
| 김지현 | Card API |
| 문정현 | Card Detail API |

# 😎 Commit Convention

| Tag Name | Description |
| --- | --- |
| feat | 기능 추가 |
| fix | 이슈 픽스 |
| test | 테스트 코드 추가 |
| refactor | 리팩토링 |
| !HOTFIX | 메인 핫 픽스 |

# 😎 Pipeline

![스크린샷 2024-01-03 오후 3 20 30](https://github.com/JamminTeam/Jamrello/assets/123870616/8553fee6-daac-4e4e-a84e-484e81a57c62)

# 😎 UI/UX

![image](https://github.com/JamminTeam/Jamrello/assets/123870616/7028a087-2ec2-4455-8965-b6fef6b6e085)

# 😎 ERD

![image](https://github.com/JamminTeam/Jamrello/assets/123870616/abe3775c-eb26-4371-9c52-9888f8ad20ba)


# 😎 API 명세서

### User API

| Name | Url | Method | Auth |
| --- | --- | --- | --- |
| 회원가입 | /api/members/signup | POST | None |
| 이메일 인증 | /api/members/email | POST | None |
| 로그인 | /api/members/login | POST | None |
| 로그아웃 | /api/members/logout | POST | Access Token |
| 내 정보 보기 | /api/members/{memberId} | GET | Access Token |
| 회원 정보 수정 | /api/members/{memberId} | PUT | Access Token |
| 회원 탈퇴 | /api/members/{memberId} | DELETE | Access Token |

### Board API

| Name | Url | Method | Auth |
| --- | --- | --- | --- |
| 보드생성 | /api/boards | Post | Access Token |
| 보드수정 | /api/boards/{boardId} | Put | Access Token |
| 보드삭제 | /api/boards/{boardId} | Delete | Access Token |
| 보드초대 | /api/boards/{boardId} | Post | Access Token |
| 보드조회 | /api/boards/{boardId} | Get | Access Token |
| 보드 백그라운드 이미지 변경 | /api/boards/{boardId} | Put | Access Token |

### Catalog API

| Name | Url | Method | Auth |
| --- | --- | --- | --- |
| 카탈로그 생성 | /api/boards/{boardId}/catalog | Post | Access Token |
| 카탈로그 이름 수정 | /api/catalog/{catalogId} | Patch | Access Token |
| 카탈로그 보관 | /api/catalog/{catalogId}/keep | Patch | Access Token |
| 카탈로그 삭제 | /api/catalog/{catalogId}/keep | Delete | Access Token |
| 카탈로그 순서 이동 | /api/catalog/{catalogId}/pos | Patch | Access Token |

### Card API

| Name | Url | Method | Auth |
| --- | --- | --- | --- |
| 카드생성 | /api/catalogs/{catalogId}/cards | Post | Access Token |
| 카드 수정 | /api/cards/{cardId} | Patch | Access Token |
| 카드 삭제 | /api/cards/{cardId} | Delete | Access Token |
| 카드 단건 조회 | /api/cards/{cardId} | Get | Access Token |
| 카드 목록 조회 | /api/catalogs/{catalogId}/cards | Get | Access Token |
| 카드 작업자 추가 | /api/cards/{cardId}/collaborators | Post | Access Token |
| 카드 작업자 삭제 | /api/cards/{cardId}/collaborators/{collaboratorId} | Delete | Access Token |
| 카드 이동 (카탈로그 내 위치 이동) | /api/cards/{cardId}/pos | Patch | Access Token |
| 카드 이동 (카탈로그 이동) | /api/cards/{cardId}/move | Patch | Access Token |

### Comment API

| Name | Url | Method | Auth |
| --- | --- | --- | --- |
| 댓글 달기 | /api/cards/{cardId}/comment | Post | Access Token |
| 댓글 수정 | /api/comment/{commentId} | Patch | Access Token |
| 댓글 삭제 | /api/comment/{commentId} | Delete | Access Token |
| 날짜 지정 | /api/card/{cardId}/due | Patch | Access Token |

# 😎 파일구조

```java
📦─ src
   📦─ main
   │  📦─ java
   │  │  📦─ com
   │  │     📦─ sparta
   │  │        📦─ jamrello
   │  │           📦─ domain
   │  │           │  📦─ board
   │  │           │  │  📦─ controller
   │  │           │  │  📦─ dto
   │  │           │  │  │  📦─ request
   │  │           │  │  │  📦─ response
   │  │           │  │  📦─ entity
   │  │           │  │  📦─ repository
   │  │           │  │  📦─ service
   │  │           │  📦─ card
   │  │           │  │  📦─ Service
   │  │           │  │  📦─ controller
   │  │           │  │  📦─ dto
   │  │           │  │  │  📦─ request
   │  │           │  │  │  📦─ response
   │  │           │  │  📦─ repository
   │  │           │  │     📦─ entity
   │  │           │  📦─ cardCollaborators
   │  │           │  │  📦─ dto
   │  │           │  │  📦─ repository
   │  │           │  │     📦─ entity
   │  │           │  📦─ catalog
   │  │           │  │  📦─ controller
   │  │           │  │  📦─ dto
   │  │           │  │  📦─ repository
   │  │           │  │  │  📦─ entity
   │  │           │  │  📦─ service
   │  │           │  📦─ comment
   │  │           │  │  📦─ controller
   │  │           │  │  📦─ dto
   │  │           │  │  📦─ repository
   │  │           │  │  │  📦─ entity
   │  │           │  │  📦─ service
   │  │           │  📦─ member
   │  │           │  │  📦─ controller
   │  │           │  │  📦─ dto
   │  │           │  │  📦─ repository
   │  │           │  │  │  📦─ entity
   │  │           │  │  📦─ service
   │  │           │  📦─ memberboard
   │  │           │     📦─ entity
   │  │           │     📦─ repository
   │  │           📦─ global
   │  │              📦─ annotation
   │  │              📦─ config
   │  │              📦─ constant
   │  │              📦─ dto
   │  │              📦─ exception
   │  │              📦─ security
   │  │              │  📦─ jwt
   │  │              📦─ time
   │  │              📦─ utils
   │  │                 📦─ s3
   │  │                    📦─ config
   │  │                    📦─ controller
   │  │                    📦─ dto
   │  │                    │  📦─ response
   │  │                    📦─ service
   │  📦─ resources
   📦─ test
      📦─ java
         📦─ com
            📦─ sparta
               📦─ jamrello
                  📦─ domain
                     📦─ board
                     │  📦─ service
                     📦─ card
                     │  📦─ Service
                     📦─ catalog
                     │  📦─ repository
                     │  │  📦─ entity
                     │  📦─ service
                     📦─ comment
                     │  📦─ controller
                     │  📦─ repository
                     │  │  📦─ entity
                     │  📦─ service
                     📦─ member
                        📦─ controller

```

# 😎 Technical Decision

1. 순서 옮기기 로직
- 삽입 정렬
- Between 벌크 연산
<br>

2. 정적 팩토리 메소드 + 빌더 패턴 사용
- 의미 있는 이름으로 메서드 명을 지을 수 있다는 장점
- 빌더 패턴을 쓸 때, 잊을 수도 있는 필드 값을 한 곳에서 관리 할 수 있다는 장점으로 위 패턴 채택

# 😎 TrubleShooting!

## 1. 통합 환경 테스트에서 환경 변수를 못읽는 오류
    
    
### 문제 정의 (원인)
    
- 사실 수집
        
  <img width="600" alt="스크린샷 2024-01-02 오전 12 14 48" src="https://github.com/JamminTeam/Jamrello/assets/123870616/267914ab-8c55-4908-a28b-7d9b25a0af73">

  <img width="600" alt="스크린샷 2024-01-02 오전 12 15 11" src="https://github.com/JamminTeam/Jamrello/assets/123870616/92e69742-8695-4303-aaa0-418551cbb431">

        
        
  통합 테스트 환경 ActiveProfiles 셋팅을 해줬는데도, 환경 변수를 못 읽어서 실행이 안되는 오류 발생
        
  <img width="600" alt="스크린샷 2024-01-02 오전 12 15 44" src="https://github.com/JamminTeam/Jamrello/assets/123870616/03cafbc9-d504-4818-805f-cbe402a73e9b">

        
  환경 변수 설정은 잘 돼있다
        
  ```bash
  java.lang.RuntimeException: Driver org.h2.Driver claims to not accept jdbcUrl, ${TEST_DB_URL}
  at com.zaxxer.hikari.util.DriverDataSource.<init>(DriverDataSource.java:110) ~[HikariCP-5.0.1.jar:na]
  at com.zaxxer.hikari.pool.PoolBase.initializeDataSource(PoolBase.java:326) ~[HikariCP-5.0.1.jar:na]
  at com.zaxxer.hikari.pool.PoolBase.<init>(PoolBase.java:112) ~[HikariCP-5.0.1.jar:na]
  at com.zaxxer.hikari.pool.HikariPool.<init>(HikariPool.java:93) ~[HikariCP-5.0.1.jar:na]
  ```
* * *
- 원인 추론
  - 테스트 환경에서 환경 변수 들어가는 타이밍이 다른 거라고 생각 된다.
    - 통합 환경 테스트가 아닌, 개발 환경(application run)에서는 잘 돌아가기 때문
* * *
### 조치 방안 검토 (분석)
    
1. @TestPropertySource 어노테이션 사용
2. Configuration 훑어보기
* * *
### 조치 방안 구현 (해결 + 느낀점)
    
1. @TestPropertySource classPath 붙여서 사용해봤지만 미해결
2. Configuration 훑어보다가 Template 만들 수 있는 걸 발견
       
<img width="600" alt="스크린샷 2024-01-02 오전 12 16 21" src="https://github.com/JamminTeam/Jamrello/assets/123870616/dafd87b6-6357-47c9-9d37-8c41dd6d67de">
    
<img width="600" alt="스크린샷 2024-01-02 오전 12 16 48" src="https://github.com/JamminTeam/Jamrello/assets/123870616/99b829f3-07dc-4956-a2ba-2ed6d5c7bfd4">

<img width="300" alt="스크린샷 2024-01-02 오전 12 17 12" src="https://github.com/JamminTeam/Jamrello/assets/123870616/50d686d7-e804-4ece-912c-0cc8a24cb800">
    
해결!<br><br>
* * *
# 2. 엔티티 연관 관계 매핑 오류

   
### 문제 정의 (원인)

- car와 catalog의 @OneToMany 연관관계 매핑에서 card save 후, catalog의 cardlist에 card가 자동 저장되지 않아 cardlist가 empty 상태인 것을 발견하였습니다.
* * *
     
### 조치 방안 검토 (분석)
        
- 일반적인 @OneToMany 관계에서는 자식 엔티티를 저장하면 부모 엔티티에 대한 변경이 발생할 때 자동으로 업데이트 됩니다. 특히 cascadeType.all을 사용하면 모든 변경이 전파됩니다.
- 그러나 fetchType이 Lazy로 설정되면 연관 엔티티들이 즉시 로드되지 않아 cardList가 즉시 업데이트 되지 않을 수 있으며 이외에도 JPA 구현 방식에 따라 자동 업데이트가 실행되지 않는 경우가 발생할 수 있음을 알게되었습니다.
  
```java
@OneToMany(mappedBy = "catalog", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Card> cardList = new ArrayList<>();

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "catalog_id")
private Catalog catalog;
```
* * *
  
### 조치 방안 구현 (해결 + 느낀점)

- 따라서 card가 save된 이후, cardlist에 add card를 직접 실행하도록 구현하는 방법으로 해결하였습니다.

```java
cardRepository.save(card);
catalog.getCardList().add(card);
```
* * *
# 3.통합테스트에서 Bean등록이 안되는 오류


### 문제 정의 (원인)
    
- 사실 수집
        
`MemberService`단 통합테스트에서 `WebConfig`의 생성 파라미터인 `jwtUtil`의 Bean이 등록이 되지않아 테스트가 진행되지 않는 문제 발생했습니다.
        
- 원인 추론
        
해당 오류를 검색한 결과 해당 오류는 `Properties`등의 설정 파일의 철자오류, 또는 `@Configuration`, `@Service` 등 Bean컨테이너로 해당 클래스가 등록이 안되있거나, 해당 클래스의 생성 파라미터의 문제가 발생한 경우 등 여러상황에서 발생할수 있다고 합니다.

* * *   
    
### 조치 방안 검토 (분석)
    
1. `jwtUtil`의 `@Configuration` 에노태이션이 철자오류없이 제대로 기입되있는지 확인한 결과 이상이 없었습니다.
2. `Properties`와 `jwtUtil`의 연결이 제대로 이뤄져있는지 확인결과 이상없이 값을 잘 불러오는것을 확인이 되었습니다.
3. `jwtUtil`의 코드를 전부 면밀히 확인해본결과 `@PostConstruct`에노테이션이 사용된 `key init`메소드에서 문제가 발생함을 확인하였습니다.
* * *    
### 조치 방안 구현 (해결 + 느낀점)
    
해당 문제를 확인결과 사용된 `jwtSecretKeY`의 경우 Base64로 인코딩하여 사용되는데 인코딩한 문자열을 입력할때 "/"철자가 오기입되면서 해당 문자를 디코딩할수없어 오류가 발생하였다는 문제를 발견했습니다.<br>
또한 문제해결과정에서 같은 시크릿키를 사용한 실제 Application환경에서는 오류가 없었는데 Application환경과 테스트환경 둘다 환경변수를 사용하여 해당 시크릿키를 입력하여 보안을 유지하고있었는데 테스트환경으로 환경변수를 일일히 복사붙여넣기하는 과정에서 "/"문자가 같이 기입된것으로 확인되었습니다.<br>
해당 문제를 막기위해 환경변수 템플릿을 활용하여 환경변수 오기입문제를 미연에 방지해야겠다는 생각이 들었습니다.
* * *
## 순환참조 오류 (spring circular reference)

![스크린샷 2024-01-03 오후 3 39 52](https://github.com/JamminTeam/Jamrello/assets/123870616/eed225bf-0683-42f1-ba68-fa56bdd7f420)


### 문제 정의 (원인)

- 순환참조 오류(spring circular reference)
- 비즈니스 로직을 수행하여 Database에 반영이 되었지만 controller에서 순환 참조 오류로 response를 뱉지 못하고 있음
* * *
### 조치 방안 검토 (분석)

1. apiResponse를 Dto로 반환해야함 → 이미 하고 있음
2. @Lazy 어노테이션을 통해서 임의로 해결 → 권장하지 않는 방법
3. 반환하는 responseDto에 member객체를 담고 있었음
* * *
### 조치 방안 구현 (해결 + 느낀점)

1. 반환값을 memberId로 변경
