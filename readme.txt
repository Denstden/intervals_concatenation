How to:
1) Run SQL scripts from sql/create.sql
2) Change properties(database properties, scheduling properties) if need in src/main/resources/application.properties
3) Run application for creating intervals "mvn clean spring-boot:run -Dspring.profiles.active=create"
4) Run application for coalescing intervals "mvn spring-boot:run"

Used libraries:
spring-boot-starter - for quick start with IoC container;
spring-boot-starter-test - for testing spring boot application;
postgresql - for working with PostgreSQL database;
commons-dbcp2 - for database connection pool;
spring-tx - for transaction management;
h2 - for testing with embedded database

Some information about algorithm:
For optimization I retrieve intervals which sorted by ascending by column start_i(for optimizing this query I created
index).
It was done in order to ignore the verification of the second condition R2.end_i >= R1.start_i, because if intervals
were sorted by start_i, it means that R2.end_i >= R2.start_i >= R1.start_i.
