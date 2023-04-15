FROM gradle:jdk17 as build
WORKDIR /
COPY build.gradle .
COPY src src
RUN gradle build -x test

FROM openjdk:17-jdk
COPY --from=build /build/libs/app.jar .
EXPOSE 8094
ENTRYPOINT ["java", "-jar", "app.jar"]