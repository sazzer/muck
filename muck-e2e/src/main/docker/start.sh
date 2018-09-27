#!/bin/sh

set -x
set -e

for BROWSER in ${BROWSERS}; do
    java -DbaseUrl=http://${WWW_HOST} -Dselenium.url=http://${SELENIUM_HOST}/wd/hub -DbrowserName=${BROWSER} -jar e2e.jar
done
