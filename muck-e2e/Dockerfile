FROM openjdk:11-jdk-slim-sid

RUN apt-get update && apt-get install -y wget

ENV DOCKERIZE_VERSION v0.6.1
RUN wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && tar -C /usr/local/bin -xzvf dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && rm dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz


ENV WWW_HOST=
ENV API_HOST=
ENV SELENIUM_HOST=
ENV NEO4J_HOST=

ARG JAR_FILE
RUN echo JAR_FILE=${JAR_FILE}

ADD ${JAR_FILE} /opt/muck/e2e.jar
ADD src/main/docker/start.sh /opt/muck/start.sh

WORKDIR /opt/muck
CMD /opt/muck/start.sh

CMD dockerize -wait tcp://${NEO4J_HOST} -wait http://${WWW_HOST} -wait http://${API_HOST}/actuator/health -wait http://${SELENIUM_HOST} -timeout 30s /opt/muck/start.sh
