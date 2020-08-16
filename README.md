# Demo Banking application

Spring security usage was not required, but it was decided to use spring security with oauth, as anyway we need to keep info about logged user.

Swaggger API - http://localhost:8080/swagger-ui.html

### Main endpoints

#### User registration:

curl --location --request POST 'http://localhost:8080/users/register' \
--header 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "demo@banking.lt",
    "password": "password"

}'

#### Login:

curl --location --request POST 'http://localhost:8080/oauth/token' \
--header 'Authorization: Basic YmFua2luZzp0aGlzaXNzZWNyZXQ=' \
--form 'username=demo@banking.lt' \
--form 'password=password' \
--form 'grant_type=password' \
--form 'scope=webclient'

#### Deposit

curl --location --request POST 'http://localhost:8080/account/transaction' \
--header 'Authorization: Bearer cb9e6d4e-4566-48eb-8822-5021b00c0bc9' \
--header 'Content-Type: application/json' \
--data-raw '{
    "amount": "2.25",
    "transactionType": "DEPOSIT"

}'

#### Statement

curl --location --request GET 'http://localhost:8080/account/statement' \
--header 'Authorization: Bearer cb9e6d4e-4566-48eb-8822-5021b00c0bc9' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "sinedas@gmail.com",
    "password": "password"

}'


### Notes

Db support
* h2 database is started together with application
* postgres is supported

### Not implemented, but future enhancements;
* Security with Jwt token
* Localization
* User can have single account, but domain can be easily changed, that user has multiple accounts
* Statement endpoint doesn't have ability to filter recors, also no pagination, nor ordering.


--------------------------
-------------------------
--------------------------


### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.3.2.RELEASE/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.3.2.RELEASE/gradle-plugin/reference/html/#build-image)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.3.2.RELEASE/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.3.2.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)
* [Flyway Migration](https://docs.spring.io/spring-boot/docs/2.3.2.RELEASE/reference/htmlsingle/#howto-execute-flyway-database-migrations-on-startup)

### Guides
The following guides illustrate how to use some features concretely:

* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

