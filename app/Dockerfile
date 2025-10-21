# Stage 1: Build
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copie les fichiers de configuration Maven
COPY pom.xml .

# Télécharge les dépendances (cache layer)
RUN mvn dependency:go-offline

# Copie le code source
COPY src ./src

# Compile et empaquete l'application
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM maven:3.9-eclipse-temurin-21

WORKDIR /app

# Copie les fichiers du projet
COPY pom.xml .
COPY src ./src

# Expose le port (par défaut Spring Boot utilise 8080)
EXPOSE 8080

# Lance Maven avec spring-boot:run en mode développement
CMD ["mvn", "spring-boot:run", "-Dspring-boot.run.arguments=--server.port=8080"]
