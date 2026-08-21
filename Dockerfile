# Stage: Runtime only
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy final JAR from Jenkins workspace (already built)
COPY target/*-SNAPSHOT.jar app.jar

 
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
