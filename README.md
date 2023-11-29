# mystream-user
mystream user service

## 요구사항

  - 회원
    - 회원 등록
    - 모든 회원은 개인의 채널을 소유하고 방송을 할 수 있음
      - 하나의 채널만을 소유 할 수 있음
    - 사용자(회원)은 채널 및 방송 관리를 위해 로그인/로그아웃 할 수 있음
    - 회원은 타 채널을 팔로일 및 팔로잉 취소를 할 수 있음
    - 회원은 타 채널을 비용을 지불하여 구독하거나 구독되어있는 채널에 대해 구독 취소 할 수 있음

## API

| name | method | path | decription | request body |
| -- | -- | -- | -- | -- |
| create | POST | /api/user/v1/user/new | create user | { "email": "test@gmail.com", "username":"user100", password : "ASDOWJSA" } |
| find | GET | /api/user/v1/user/{id} | find user | |
| updateUseProfile | PATCH | /api/user/v1/user/{id}/profile | change value of channel description | { "id": 100, "email" : "tesg1@gmail.com", "username" : "testg1" } |
| follow | PUT | /api/user/v1/following/follow | follow channel | { "userId" : 100, "channelId" : 105 } |
| unfollow | PUT | /api/user/v1/following/unfollow | unfollow channel | { "userId" : 100, "channelId" : 105 } |
