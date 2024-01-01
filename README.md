## 😎 Jamrello - 재밌는 잼렐로
<img width="512" alt="스크린샷 2024-01-02 오전 12 14 48" src="https://github.com/JamminTeam/Jamrello/assets/123870616/195e0e84-5225-4ed3-919d-1b9bf1c1aaf6"> <br>
오늘 일은 내일로? 잼렐로와 함께면 바로바로!

## 😎 시연 영상

## 😎 Links

<a href="https://documenter.getpostman.com/view/30896712/2s9YsDiuMq"> PostMan </a> <br>
<a href="https://www.notion.so/10-ee5b850bfa1a44ea8dc9758b10dc957a?pvs=4"> Notion </a>

## 😎 Role

| Name | Role |
| --- | --- |
| 조원호 | Catalog API |
| 안준우 | User API, Auth API |
| 박상신 | Board API |
| 김지현 | Card API |
| 문정현 | Card Detail API |

## 😎 Commit Convention

| Tag Name | Description |
| --- | --- |
| feat | 기능 추가 |
| fix | 이슈 픽스 |
| test | 테스트 코드 추가 |
| refactor | 리팩토링 |
| !HOTFIX | 메인 핫 픽스 |

## 😎 UI/UX

![image](https://github.com/JamminTeam/Jamrello/assets/123870616/7028a087-2ec2-4455-8965-b6fef6b6e085)


## 😎 ERD

![image](https://github.com/JamminTeam/Jamrello/assets/123870616/abe3775c-eb26-4371-9c52-9888f8ad20ba)


## 😎 API 명세서

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

## 😎 파일구조

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

## 😎 Technical Decision

1. 순서 옮기기 로직
- 삽입 정렬
- Between 벌크 연산

2. 정적 팩토리 메소드 + 빌더 패턴 사용
- 의미 있는 이름으로 메서드 명을 지을 수 있다는 장점
- 빌더 패턴을 쓸 때, 잊을 수도 있는 필드 값을 한 곳에서 관리 할 수 있다는 장점으로 위 패턴 채택

## 😎 TrubleShooting!

1. 통합 환경 테스트에서 환경 변수를 못읽는 오류
    
    
    ## 문제 정의 (원인)
    
    - 사실 수집
        
        <img width="1635" alt="스크린샷 2024-01-02 오전 12 14 48" src="https://github.com/JamminTeam/Jamrello/assets/123870616/267914ab-8c55-4908-a28b-7d9b25a0af73">

        <img width="1643" alt="스크린샷 2024-01-02 오전 12 15 11" src="https://github.com/JamminTeam/Jamrello/assets/123870616/92e69742-8695-4303-aaa0-418551cbb431">

        
        
        통합 테스트 환경 ActiveProfiles 셋팅을 해줬는데도, 환경 변수를 못 읽어서 실행이 안되는 오류 발생
        
        <img width="1652" alt="스크린샷 2024-01-02 오전 12 15 44" src="https://github.com/JamminTeam/Jamrello/assets/123870616/03cafbc9-d504-4818-805f-cbe402a73e9b">

        
        환경 변수 설정은 잘 돼있다
        
        ```bash
        java.lang.RuntimeException: Driver org.h2.Driver claims to not accept jdbcUrl, ${TEST_DB_URL}
        at com.zaxxer.hikari.util.DriverDataSource.<init>(DriverDataSource.java:110) ~[HikariCP-5.0.1.jar:na]
        at com.zaxxer.hikari.pool.PoolBase.initializeDataSource(PoolBase.java:326) ~[HikariCP-5.0.1.jar:na]
        at com.zaxxer.hikari.pool.PoolBase.<init>(PoolBase.java:112) ~[HikariCP-5.0.1.jar:na]
        at com.zaxxer.hikari.pool.HikariPool.<init>(HikariPool.java:93) ~[HikariCP-5.0.1.jar:na]
        ```
        
    - 원인 추론
        - 테스트 환경에서 환경 변수 들어가는 타이밍이 다른 거라고 생각 된다.
            - 통합 환경 테스트가 아닌, 개발 환경(application run)에서는 잘 돌아가기 때문
    
    ## 조치 방안 검토 (분석)
    
    1. @TestPropertySource 어노테이션 사용
    2. Configuration 훑어보기
    
    ## 조치 방안 구현 (해결 + 느낀점)
    
    1. @TestPropertySource classPath 붙여서 사용해봤지만 미해결
    2. Configuration 훑어보다가 Template 만들 수 있는 걸 발견
       
    <img width="1116" alt="스크린샷 2024-01-02 오전 12 16 21" src="https://github.com/JamminTeam/Jamrello/assets/123870616/dafd87b6-6357-47c9-9d37-8c41dd6d67de">
    
    <img width="1144" alt="스크린샷 2024-01-02 오전 12 16 48" src="https://github.com/JamminTeam/Jamrello/assets/123870616/99b829f3-07dc-4956-a2ba-2ed6d5c7bfd4">

    <img width="435" alt="스크린샷 2024-01-02 오전 12 17 12" src="https://github.com/JamminTeam/Jamrello/assets/123870616/50d686d7-e804-4ece-912c-0cc8a24cb800">
    
    해결!
