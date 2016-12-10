FROM java:alpine

VOLUME /tmp

WORKDIR /app

COPY target/springboot-angular2-tutorial-0.1.0.jar app.jar
RUN sh -c 'touch app.jar'

COPY docker/newrelic.jar .
COPY docker/newrelic.yml .

ENV JAVA_OPTS="-javaagent:newrelic.jar"

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS \
-Djava.security.egd=file:/dev/./urandom \
-Dnewrelic.environment=$SPRING_PROFILES_ACTIVE \
-jar app.jar" ]

EXPOSE 8080
