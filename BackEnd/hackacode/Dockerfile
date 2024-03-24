FROM openjdk:21-slim

LABEL author=grupo11.4cod

ENV DATABASE_URL jdbc\:mysql\://localhost:3306/hackacode\?serverTimezone\=UTC&createDatabaseIfNotExist=true
ENV DATABASE_USERNAME root
ENV DATABASE_PASSWORD "123"
ENV DATABASE_PLATAFORM org.hibernate.dialect.MySQL8Dialect
ENV DATABASE_DRIVER com.mysql.cj.jdbc.Driver

COPY target/hackacode-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]