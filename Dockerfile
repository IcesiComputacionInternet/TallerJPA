FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY target/accountSystem-0.0.1-SNAPSHOT.jar /app/demo.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]