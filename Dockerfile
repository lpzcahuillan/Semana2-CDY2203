FROM eclipse-temurin:17-jre-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
RUN addgroup -S app && adduser -S -G app app
USER app
ENTRYPOINT ["java","-jar","/app.jar"]