- registration  login:password
- lukasz.bak@interiowy.pl:lukasz  ->  role office

- **[Swagger UI - API Documentation (Registration) ](http://51.79.251.86:8090/webjars/swagger-ui/index.html)**

- host:port/user/registration  ->register  new person (role office,teacher,student)  -> only user with role office allowed access

 - login endpoint
 - curl -X POST http://51.79.251.86:8090/login -H "Content-Type: application/json" -d "{\"email\": \"lukasz.bak@interiowy.pl\", \"password\": \"lukasz\"}"
   
- available roles -> only user with role office allowed access
- curl -X GET http://51.79.251.86:8090/user/role -H "Authorization: Bearer use login enpoint"


- curl -X POST http://51.79.251.86:8090/user/registration -H "Content-Type: application/json" -H "Authorization: Bearer use login enpoint " -d "{
    \"firstName\":\"John\",
    \"lastName\": \"Doe\",
    \"email\": \"johndoe@example.com\",
    \"password\": \"yourpassword\",
    \"role\": \"Teacher\"
  }"
- 








- **[Swagger UI - API Documentation](http://51.79.251.86:8081/webjars/swagger-ui/index.html)**

- **[Swagger UI - API Documentation](http://51.79.251.86:8082/webjars/swagger-ui/index.html)**

- **[Swagger UI - API Documentation](http://51.79.251.86:8083/webjars/swagger-ui/index.html)**

- **[Swagger UI - API Documentation](http://51.79.251.86:8084/webjars/swagger-ui/index.html)**

 - **[Swagger UI - API Documentation](http://51.79.251.86:8085/webjars/swagger-ui/index.html)**
