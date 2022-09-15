FROM artifactory.apps.bancolombia.com/devops/openjdk11-openj9:x86_64-alpine-jdk-11.0.7_10_openj9-0.20.0-slim
VOLUME /tmp
COPY *.jar app.jar
ENV JAVA_OPTS=" -Xshareclasses:name=cacheapp,cacheDir=/cache,nonfatal -XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"
USER appuser
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS  -jar app.jar" ]