# BUILD SOURCE
FROM gradle:7.6.0-jdk17 AS BUILD_IMAGE
ENV APP_HOME=/root/dev/spectacle
WORKDIR $APP_HOME

# COPY GRADLE FILES
COPY gradle.properties build.gradle.kts settings.gradle.kts $APP_HOME/
COPY gradle $APP_HOME/gradle
COPY spectacle-common/build.gradle.kts $APP_HOME/spectacle-common/build.gradle.kts
COPY convention-plugins/build.gradle.kts $APP_HOME/convention-plugins/build.gradle.kts
COPY convention-plugins/src/main/kotlin/convention.publication.gradle.kts $APP_HOME/convention-plugins/src/main/kotlin/convention.publication.gradle.kts
COPY spectacle-central/application/build.gradle.kts $APP_HOME/spectacle-central/application/build.gradle.kts
COPY spectacle-central/webapp/build.gradle.kts $APP_HOME/spectacle-central/webapp/build.gradle.kts
COPY spectacle-central/common/build.gradle.kts $APP_HOME/spectacle-central/common/build.gradle.kts
COPY spectacle-central/domain/build.gradle.kts $APP_HOME/spectacle-central/domain/build.gradle.kts
COPY spectacle-dsl/spectacle-dsl-bdd/build.gradle.kts $APP_HOME/spectacle-dsl/spectacle-dsl-bdd/build.gradle.kts
COPY spectacle-dsl/spectacle-dsl-assertions/build.gradle.kts $APP_HOME/spectacle-dsl/spectacle-dsl-assertions/build.gradle.kts
COPY spectacle-dsl/spectacle-dsl-publisher/build.gradle.kts $APP_HOME/spectacle-dsl/spectacle-dsl-publisher/build.gradle.kts
COPY spectacle-dsl/spectacle-dsl-http/build.gradle.kts $APP_HOME/spectacle-dsl/spectacle-dsl-http/build.gradle.kts
COPY spectacle-dsl/spectacle-dsl-protobuf/build.gradle.kts $APP_HOME/spectacle-dsl/spectacle-dsl-protobuf/build.gradle.kts

# DOWNLOAD DEPENDENCIES
RUN gradle build -x test --continue

# BUILD PROJECT
COPY . .
RUN gradle stage

FROM eclipse-temurin:17-jre-alpine AS RUNTIME_IMAGE
WORKDIR /root/
COPY --from=BUILD_IMAGE /root/dev/spectacle/spectacle-central/application/build/install/application .
EXPOSE 8080:8080

CMD ["./bin/application"]
