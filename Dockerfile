# 1. బిల్డ్ స్టేజ్: మావెన్ మరియు టెమురిన్ జావా 21తో బిల్డ్ చేయడం
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# 2. రన్ స్టేజ్: జావా 21 రన్ టైమ్ ఎన్విరాన్మెంట్ (JRE 21)
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8089
ENTRYPOINT ["java", "-jar", "app.jar"]