FROM gradle:8.12-jdk21 AS build
WORKDIR /workspace

COPY gradle gradle
COPY gradlew .
COPY settings.gradle build.gradle ./
COPY src src
COPY config config

RUN chmod +x gradlew \
	&& ./gradlew --no-daemon clean installDist \
		-x test \
#		-x integrationTest \
		-x checkstyleMain \
		-x checkstyleTest \
		-x checkstyleIntegrationTest

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /workspace/build/install/java-template /app/

ENTRYPOINT ["/app/bin/java-template"]
