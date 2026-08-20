# Build multi-stage: la etapa "build" compila el jar con una imagen que trae
# Maven + JDK 21; la imagen final solo contiene el artefacto ya compilado sobre un
# runtime liviano. Esto permite `docker compose up --build` autocontenido, sin
# depender de tener Maven/JDK instalados en el host (a diferencia del
# src/main/docker/Dockerfile.jvm generado por Quarkus, que asume que ya corriste
# `mvn package` manualmente antes de construir la imagen).
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build
COPY pom.xml .
# Descarga de dependencias en su propia capa (cacheable): mientras no cambie el
# pom.xml, Docker reutiliza esta capa aunque cambie el código en src/.
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q package -DskipTests

# Misma imagen runtime que usa el Dockerfile.jvm generado por Quarkus
# (ubi9/openjdk-21-runtime) — solo cambia de dónde viene el jar.
FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:1.24
ENV LANGUAGE='en_US:en'

# Se copia el resultado del build en 4 capas (lib/app/quarkus/jar) para que un
# cambio de código no invalide la capa de librerías de terceros.
COPY --chown=185 --from=build /build/target/quarkus-app/lib/ /deployments/lib/
COPY --chown=185 --from=build /build/target/quarkus-app/*.jar /deployments/
COPY --chown=185 --from=build /build/target/quarkus-app/app/ /deployments/app/
COPY --chown=185 --from=build /build/target/quarkus-app/quarkus/ /deployments/quarkus/

EXPOSE 8080
USER 185
ENV JAVA_OPTS_APPEND="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"

ENTRYPOINT [ "/opt/jboss/container/java/run/run-java.sh" ]
