FROM gradle:8.12-jdk21 AS build
WORKDIR /workspace

COPY gradle gradle
COPY gradlew .
COPY settings.gradle build.gradle ./

RUN chmod +x gradlew \
	&& ./gradlew --no-daemon dependencies --configuration compileClasspath

COPY src src
COPY config config

RUN ./gradlew --no-daemon clean bootJar \
		-x test \
		-x integrationTest \
		-x checkstyleMain \
		-x checkstyleTest \
		-x checkstyleIntegrationTest

FROM eclipse-temurin:21-jre
WORKDIR /app

RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser

COPY --from=build /workspace/build/libs/*.jar app.jar

USER appuser

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
