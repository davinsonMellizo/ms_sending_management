FROM #{java_11_alpine}#
LABEL version="#{Build.BuildNumber}#" maintainer="#{Email}#" description="..."
EXPOSE 8080
COPY --chown=#{java_user}#:#{java_user}# app-service.jar app.jar
ENV JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom"
USER #{alpine_user}#
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]