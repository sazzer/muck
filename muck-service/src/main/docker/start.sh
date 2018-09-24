#!/bin/sh

if [ -f /opt/muck/application.properties.template ]; then
    envsubst < /opt/muck/application.properties.template > /opt/muck/application.properties
fi

java -jar service.jar --spring.config.location=file:/opt/muck/application.properties
