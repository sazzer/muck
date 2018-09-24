FROM openjdk:10.0.1-10-jdk-slim-sid

RUN apt update && apt install -y curl gettext-base

ENV PORT=8080
ENV NEO4J_URI=
ENV MUCK_GOOGLE_ENABLED=false
ENV MUCK_GOOGLE_AUTH_URI=
ENV MUCK_GOOGLE_TOKEN_URI=
ENV MUCK_GOOGLE_REDIRECT_URI=
ENV MUCK_GOOGLE_CLIENT_ID=
ENV MUCK_GOOGLE_CLIENT_SECRET=

ARG JAR_FILE
RUN echo JAR_FILE=${JAR_FILE}

ADD ${JAR_FILE} /opt/muck/service.jar
ADD src/main/docker/application.properties.template /opt/muck/application.properties.template
ADD src/main/docker/start.sh /opt/muck/start.sh

WORKDIR /opt/muck
CMD /opt/muck/start.sh

HEALTHCHECK CMD curl -f http://localhost:8080/actuator/health || exit 1
