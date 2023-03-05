FROM openjdk:13-jdk
VOLUME /tmp
COPY *.jar  /app.jar
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar
