FROM nginx:1.15.0-alpine

RUN apk --no-cache add curl
ENV MUCK_SERVICE_API_URI=

COPY build /usr/share/nginx/html
COPY docker/start.sh /start.sh
RUN chmod +x /start.sh

CMD /start.sh

HEALTHCHECK CMD curl -f http://localhost || exit 1
