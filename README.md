
#  pcf-resource-calculate
 
- 단일/다수 Pivotal Cloud Foundry의 모든 Org/Space에 가동되어있는 Application/Instance 수를 전체 조회 

## 1. 사용 방법

### 1.1. git clone
```
https://github.com/do-workspace/pcf-resource-calculate.git
```

### 1.2. maven update


### 1.3. pcf resource file modify
```
pcf-resource-calculate/src/main/resources/pcf_auth_info.json
{
  "pcf_admin_info": [
    {
      "env": "cf_alias_name",
      "api": "cf_domain",
      "username": "cf_username",
      "password": "cf_password"
    },
    {
      "env": "cf_alias_name",
      "api": "cf_domain",
      "username": "cf_username",
      "password": "cf_password"
    },
    {
      "env": "cf_alias_name",
      "api": "cf_domain",
      "username": "cf_username",
      "password": "cf_password"
    }
  ]
}
```
- 단일 cloud foundry일 경우 나머지 정보를 삭제 해야 한다.

### 1.4. run Spring Boot App


### 1.5. localhost:8080
