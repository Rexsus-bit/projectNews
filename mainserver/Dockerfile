FROM amazoncorretto:17-alpine-jdk
COPY build/libs/*exec.jar main.jar
ENTRYPOINT ["java","-jar","/main.jar"]