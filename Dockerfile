# 1. బిల్డ్ స్టేజ్: మావెన్ సహాయంతో జార్ ఫైల్ తయారు చేయడం
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# 2. రన్ స్టేజ్: తయారైన జార్ ఫైల్ ని క్లౌడ్ లో రన్ చేయడం
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8089
ENTRYPOINT ["java", "-jar", "app.jar"]