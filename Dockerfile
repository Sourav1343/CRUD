# Stage 1: Build jar using the structure Jenkins already created
FROM eclipse-temurin:17-jdk AS build
WORKDIR /workspace

# Copy only required files
COPY pom.xml .
COPY src ./src

# Run Maven using system maven (Jenkins pipeline provides Maven)
RUN mvn -B -DskipTests clean package

# Stage 2: Create runtime image
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy final jar from the build stage
COPY --from=build /workspace/target/*-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
