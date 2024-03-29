# docker build --build-arg --no-cache -t "test-service" -f "./Dockerfile" .
# docker tag test-service acrpetcliaks.azurecr.io/petclinic/test-service
# docker push acrpetcliaks.azurecr.io/petclinic/test-service
# docker pull acrpetcliaks.azurecr.io/petclinic/test-service
# docker image ls
# docker run -p 8080:8080 -p 8081:8081 test-service
# docker run --env SPRING_PROFILES_ACTIVE=toto -p 8080:8080 -p 8081:8081  test-service
# docker container ls
# docker ps
# docker exec -it <container-id> /bin/sh

# https://docs.microsoft.com/en-us/java/openjdk/containers
FROM mcr.microsoft.com/openjdk/jdk:11-mariner as builder
ENV APPI_VERSION="3.4.8"
ENV APP_INSIGHTS_AGENT_JAR_FILE_PATH="/tmp/app/applicationinsights-agent-${APPI_VERSION}.jar"
LABEL Maintainer="pinpin <noname@microsoft.com>"
LABEL Description="Java Spring Boot microservice built from MS OpenJDK 11-Mariner"
RUN mkdir /tmp/app
WORKDIR /tmp/app
COPY "./target/*.jar" /tmp/app/app.jar
RUN java -Djarmode=layertools -jar "/tmp/app/app.jar" extract
RUN curl -SL --output ${APP_INSIGHTS_AGENT_JAR_FILE_PATH} https://github.com/microsoft/ApplicationInsights-Java/releases/download/${APPI_VERSION}/applicationinsights-agent-${APPI_VERSION}.jar

FROM mcr.microsoft.com/openjdk/jdk:11-mariner
WORKDIR /tmp/app
ENV APPI_VERSION="3.4.8"
ENV APP_INSIGHTS_AGENT_JAR_FILE_PATH="/tmp/app/applicationinsights-agent-${APPI_VERSION}.jar"
ENV APPLICATIONINSIGHTS_CONFIGURATION_FILE="BOOT-INF/classes/applicationinsights.json"
ENV SPRING_PROFILES_ACTIVE="docker,mysql"
# https://github.com/Azure/azure-cli/issues/20413
# https://github.com/Azure/acr-build/issues/10
# https://stackoverflow.com/questions/51115856/docker-failed-to-export-image-failed-to-create-image-failed-to-get-layer
# https://github.com/moby/moby/issues/37965
COPY --from=builder ${APP_INSIGHTS_AGENT_JAR_FILE_PATH}/ ./
COPY --from=builder /tmp/app/dependencies/ ./
COPY --from=builder /tmp/app/snapshot-dependencies/ ./
RUN true
COPY --from=builder /tmp/app/spring-boot-loader/ ./
RUN true
COPY --from=builder /tmp/app/application/ ./
EXPOSE 80 8080 8081 8082 8083 8084 8888 9090

# "-javaagent:/tmp/app/applicationinsights-agent-3.4.8.jar", 
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher", "--server.port=8080"]