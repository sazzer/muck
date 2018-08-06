#!/bin/sh

cat << EOF > /usr/share/nginx/html/config.js
window.configuration = {
};
EOF

nginx -g 'daemon off;'
