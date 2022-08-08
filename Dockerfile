# BUILD SOURCE
FROM openjdk:11.0-jdk AS BUILD_IMAGE
ENV APP_HOME=/root/dev/spectacle
WORKDIR $APP_HOME
# MAKE PROJECT STRUCTURE
RUN mkdir -p $APP_HOME/spectacle-common/src/commonMain/kotlin
RUN mkdir -p $APP_HOME/convention-plugins/src/main/kotlin
RUN mkdir -p $APP_HOME/spectacle-central/application/src/commonMain/kotlin
RUN mkdir -p $APP_HOME/spectacle-central/domain/src/main/kotlin
RUN mkdir -p $APP_HOME/spectacle-dsl/src/commonMain/kotlin
# COPY GRADLE FILES
COPY build.gradle.kts settings.gradle.kts gradlew gradlew.bat $APP_HOME
COPY gradle $APP_HOME/gradle
COPY spectacle-common/build.gradle.kts $APP_HOME/spectacle-common/build.gradle.kts
COPY convention-plugins/build.gradle.kts $APP_HOME/convention-plugins/build.gradle.kts
COPY convention-plugins/src/main/kotlin/convention.publication.gradle.kts $APP_HOME/convention-plugins/src/main/kotlin/convention.publication.gradle.kts
COPY spectacle-central/application/build.gradle.kts $APP_HOME/spectacle-central/application/build.gradle.kts
COPY spectacle-central/webapp/build.gradle.kts $APP_HOME/spectacle-central/webapp/build.gradle.kts
COPY spectacle-central/common/build.gradle.kts $APP_HOME/spectacle-central/common/build.gradle.kts
COPY spectacle-central/domain/build.gradle.kts $APP_HOME/spectacle-central/domain/build.gradle.kts
COPY spectacle-dsl/build.gradle.kts $APP_HOME/spectacle-dsl/build.gradle.kts
# DOWNLOAD DEPENDENCIES
RUN ./gradlew build -x test --continue
# BUILD PROJECT
COPY . .
RUN ./gradlew stage

FROM openjdk:11.0-jre
WORKDIR /root/
COPY --from=BUILD_IMAGE /root/dev/spectacle/spectacle-central/application/build/install/application .
EXPOSE 8080:8080

ENV DATABASE_URL=""
ENV DATABASE_HOST=""
ENV DATABASE_PORT=""
ENV ENVDATABASE_NAME=""
ENV DATABASE_USERNAME=""
ENV DATABASE_PASSWORD=""

CMD ["./bin/application"]