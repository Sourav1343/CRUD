# Stage 1: build the app
FROM eclipse-temurin:17-jdk AS build
WORKDIR /workspace

# copy maven files and download dependencies first (layer caching)
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw mvnw
RUN chmod +x mvnw || true

# If mvnw is not present in repo, fall back to system maven (the pipeline will provide maven)
# Copy sources and build
COPY src src
RUN if [ -x mvnw ]; then ./mvnw -B -DskipTests clean package; else mvn -B -DskipTests clean package; fi

# Stage 2: runtime image
FROM eclipse-temurin:17-jre
ARG JAR_FILE=target/*-SNAPSHOT.jar
WORKDIR /app
COPY --from=build /workspace/${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
