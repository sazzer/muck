FROM openjdk:10.0.1-10-jdk-slim-sid

RUN apt update && apt install -y curl

ENV NEO4J_URI=

ARG JAR_FILE
RUN echo JAR_FILE=${JAR_FILE}

ENV PORT=8080

ADD ${JAR_FILE} /opt/muck/service.jar
WORKDIR /opt/muck
CMD java -Dspring.data.neo4j.uri="$NEO4J_URI" -Dserver.port="$PORT" -Dspring.profiles.active=prod -jar service.jar

HEALTHCHECK CMD curl -f http://localhost:8080/actuator/health || exit 1
