# Sping Boot OAuth2 Demo

Spring Boot Rest API Secured with OAuth2 (Password Grant) through integrated Authorization and Resource Server.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

* JDK 8 or above.
* Maven
* Postman (Chrome plugin or standalone installation)
* GIT CLI (Optional)

### Installing and Running the App

1. Clone or download this repo

```
git clone https://github.com/nanda-dev/springboot-oauth2-demo.git
```

2. CD into the project root

```
cd springboot-oauth2-demo
```

3. Run the app

```
mvn spring-boot:run
```


## Running the Demo Features

### Obtain OAuth Access Token

Hit the following URL in Postman using the params specified:

```
URL: http://localhost:8080/oauth/token
Method: POST
Authorization:
  Type: Basic Auth
  Username: client
  Password: secret
Body:
  Type: x-www-form-urlencoded
  Fields:
    grant_type: password
    username: user
    password: pwd

```

You should get a response similar to the one given below:

```
{
    "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsib2F1dGgyLXJlc291cmNlIl0sInVzZXJfbmFtZSI6ImFkbWluIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl0sImV4cCI6MTU2ODQ1MDAyMiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJqdGkiOiJhNGY5MDhkYi0xZDRiLTQ1ZDMtOTYzYi1lOWNmYzgyMDE2ZDUiLCJjbGllbnRfaWQiOiJjbGllbnQifQ.kgUiSgTi8PPQhiVpEXc1eTBWl4_jeUlblyVYAOb_EaE",
    "token_type": "bearer",
    "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsib2F1dGgyLXJlc291cmNlIl0sInVzZXJfbmFtZSI6ImFkbWluIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl0sImF0aSI6ImE0ZjkwOGRiLTFkNGItNDVkMy05NjNiLWU5Y2ZjODIwMTZkNSIsImV4cCI6MTU2ODY5NzUyMiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJqdGkiOiIxZjM2ZDk0Yi0wNDE3LTRhMmYtOWQ0Mi00NmQ0YjcyOThlNzMiLCJjbGllbnRfaWQiOiJjbGllbnQifQ.E6Uz9ltNATchHadE23E5Ugze5mYeddu1McQGG3KDJho",
    "expires_in": 2499,
    "scope": "read write",
    "jti": "a4f908db-1d4b-45d3-963b-e9cfc82016d5"
}
```

### Access secured resource

Hit the API endpoint that is secured to be accessed by Users of specific 'Role's. An examle is given below.

First, copy the 'access_token' from the /oauth/token API response from the above step for the username: 'user', who has the Role: 'USER' assigned to it.

Use this 'access_token' to access the following URL through Postman.

The access_token is to be provided as the value for the Header Key: 'Authorization', and the value should be in the following format:
'Bearer&lt;space>&lt;access_token>'


```
URL: http://localhost:8080/api/users/me
Method: GET
Headers:
  Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsib2F1dGgyLXJlc291cmNlIl0sInVzZXJfbmFtZSI6InVzZXIiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiZXhwIjoxNTY4NDQ5OTgwLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwianRpIjoiZDUyNThiYWItOWJjZS00NjEwLWIxMDgtOTE5ZjgyN2E5MDc4IiwiY2xpZW50X2lkIjoiY2xpZW50In0.iAmhDgW4me65_vFtzcnTVPPvFYq4BNL8PELxHyLuSfY
```

You should get the following response:

```
testuser@demo.com
```

### Other Operations

## Validate Token

Hit the following URL in Postman using the params specified:

```
URL: http://localhost:8080/oauth/check_token
Method: POST
Authorization:
  Type: Basic Auth
  Username: client
  Password: secret
Body:
  Type: x-www-form-urlencoded
  Fields:
    token: <paste-access_token-value-here>

```
You should get a response similar to the one given below:

```
{
    "aud": [
        "oauth2-resource"
    ],
    "user_name": "admin",
    "scope": [
        "read",
        "write"
    ],
    "exp": 1568452760,
    "authorities": [
        "ROLE_ADMIN"
    ],
    "jti": "ba873ffc-f896-4aaf-a5d7-a09d34b5362f",
    "client_id": "client"
}
```
## Generate Access Token from Refresh Token

Hit the following URL in Postman using the params specified:

NOTE: 'refresh_token' value needs to be taken from the /oath/token API response which gave you the 'access_token' value. Whenever an 'access_token' is generated, the API response would include the corresponding 'refresh_token' as well, to be used later to obtain new 'access_token' once the previous one gets expired, and without transmitting the username/password details repeatedly.

```
URL: http://localhost:8080/oauth/token
Method: POST
Authorization:
  Type: Basic Auth
  Username: client
  Password: secret
Body:
  Type: x-www-form-urlencoded
  Fields:
    grant_type: refresh_token
    refresh_token: <paste-refresh_token-value-here>

```
You should get a response similar to the one given below:

```
{
    "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsib2F1dGgyLXJlc291cmNlIl0sInVzZXJfbmFtZSI6ImFkbWluIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl0sImV4cCI6MTU2ODQ1Mjc2MCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJqdGkiOiJiYTg3M2ZmYy1mODk2LTRhYWYtYTVkNy1hMDlkMzRiNTM2MmYiLCJjbGllbnRfaWQiOiJjbGllbnQifQ.JHlX0MC7CsmCuhv6TqixRtmZzRwoG16Y4bPudOvre6o",
    "token_type": "bearer",
    "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsib2F1dGgyLXJlc291cmNlIl0sInVzZXJfbmFtZSI6ImFkbWluIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl0sImF0aSI6ImJhODczZmZjLWY4OTYtNGFhZi1hNWQ3LWEwOWQzNGI1MzYyZiIsImV4cCI6MTU2ODcwMDI0NiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJqdGkiOiJmOGVkMmEzMS0yNmZhLTRlZTgtYjJkYi0zZDlmYTk3NmE1NDYiLCJjbGllbnRfaWQiOiJjbGllbnQifQ.StdxRohxWDg2-blzY40Wof8lKOW1WtA4EAQb1I6QTrI",
    "expires_in": 2499,
    "scope": "read write",
    "jti": "ba873ffc-f896-4aaf-a5d7-a09d34b5362f"
}
```

## Built With
* JDK 8
* Maven 3.5.2
* Spring Boot 2.1.8.RELEASE
* org.springframework.security.oauth.boot: spring-security-oauth2-autoconfigure: 2.1.8.RELEASE
* STS 3.9.9 [Primary IDE]
* Atom 1.40.1 [With 'markdown-writer' package to create/edit README.md file]

## Authors

* **Nanda Gopan** - *Initial work* - [nanda-dev@github](https://github.com/nanda-dev)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

Following work(s) were referred to for creating this app:

* [howtodoinjava: OAuth2 Authorization and Resource Server](https://howtodoinjava.com/spring5/security5/oauth2-auth-server/) - A form-based authorization_code grant_type implementation, which was then updated to implement the password grant_type implementation that we have with us now.
* [PurpleBooth/README-Template.md](https://gist.github.com/PurpleBooth/109311bb0361f32d87a2) - Sample README.md template that helped frame this basic Readme documentation.
