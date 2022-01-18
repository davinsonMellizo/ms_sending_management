FROM artifactory.apps.bancolombia.com/devops/openjdk11-openj9:x86_64-alpine-jdk-11.0.7_10_openj9-0.20.0-slim
USER root
ADD *.jar app.jar
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=70"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar" ]
