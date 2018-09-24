#!/bin/sh

cat << EOF > /usr/share/nginx/html/config.js
window.ConfigurationOptions = {
    API_URI: '${MUCK_SERVICE_API_URI}'
};
EOF

nginx -g 'daemon off;'
