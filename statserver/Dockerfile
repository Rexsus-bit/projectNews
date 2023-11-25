FROM amazoncorretto:17-alpine-jdk
COPY build/libs/*exec.jar statistics.jar
ENTRYPOINT ["java","-jar","/statistics.jar"]