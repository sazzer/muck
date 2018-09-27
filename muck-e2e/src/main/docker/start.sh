#!/bin/sh

set -x
set -e

for BROWSER in ${BROWSERS}; do
    java -Durl.base=http://${WWW_HOST} -Durl.selenium=http://${SELENIUM_HOST}/wd/hub -DbrowserName=${BROWSER} -Ddir.screenshot=./target/screenshots/${BROWSER} -jar e2e.jar
done
