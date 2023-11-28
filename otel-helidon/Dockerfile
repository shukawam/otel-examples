FROM container-registry.oracle.com/java/openjdk:21 as build
WORKDIR /usr/share
RUN set -x && \
    curl -O https://archive.apache.org/dist/maven/maven-3/3.8.4/binaries/apache-maven-3.8.4-bin.tar.gz && \
    tar -xvf apache-maven-*-bin.tar.gz  && \
    rm apache-maven-*-bin.tar.gz && \
    mv apache-maven-* maven && \
    ln -s /usr/share/maven/bin/mvn /bin/
WORKDIR /helidon
ADD pom.xml pom.xml
RUN mvn package -Dmaven.test.skip -Declipselink.weave.skip -Declipselink.weave.skip -DskipOpenApiGenerate
ADD src src
RUN mvn package -DskipTests
RUN echo "done!"

FROM busybox as download
WORKDIR /var/cache
RUN wget https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v1.27.0/opentelemetry-javaagent.jar
RUN echo "done! - download stage"

FROM container-registry.oracle.com/java/openjdk:21
WORKDIR /helidon
COPY --from=build /helidon/target/otel-helidon.jar ./
COPY --from=build /helidon/target/libs ./libs
COPY --from=download /var/cache ./agent
CMD ["java", "-javaagent:/helidon/agent/opentelemetry-javaagent.jar", "-jar", "otel-helidon.jar"]
EXPOSE 8080
