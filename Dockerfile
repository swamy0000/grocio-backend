# 1. బిల్డ్ స్టేజ్: మావెన్ మరియు టెమురిన్ జావాతో బిల్డ్ చేయడం
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# 2. రన్ స్టేజ్: కేవలం రన్ టైమ్ ఎన్విరాన్మెంట్ (JRE) మాత్రమే వాడటం వల్ల సర్వర్ వేగంగా రన్ అవుతుంది
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8089
ENTRYPOINT ["java", "-jar", "app.jar"]