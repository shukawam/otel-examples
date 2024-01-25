FROM maven:3.9.5 as build
WORKDIR /helidon
ADD pom.xml pom.xml
RUN mvn package -Dmaven.test.skip -Declipselink.weave.skip -Declipselink.weave.skip -DskipOpenApiGenerate
ADD src src
RUN mvn package -DskipTests
RUN echo "done!"

FROM busybox as download
WORKDIR /var/cache
RUN wget https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v1.32.0/opentelemetry-javaagent.jar
RUN echo "done! - download stage"

FROM eclipse-temurin:21.0.2_13-jre
WORKDIR /helidon
COPY --from=build /helidon/target/otel-helidon.jar ./
COPY --from=build /helidon/target/libs ./libs
COPY --from=download /var/cache ./agent
ENV JAVA_TOOL_OPTIONS=-javaagent:/helidon/agent/opentelemetry-javaagent.jar
CMD ["java", "-jar", "otel-helidon.jar"]
EXPOSE 8080
