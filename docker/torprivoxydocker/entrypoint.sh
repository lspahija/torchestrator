#!/bin/sh


sed -i "s/9050/$1/g" /etc/service/tor/torrc
sed -i "s/9051/$2/g" /etc/service/tor/torrc
sed -i "s/9050/$1/g" /etc/service/privoxy/config
sed -i "s/8118/$3/g" /etc/service/privoxy/config

cat /etc/service/tor/torrc
cat /etc/service/privoxy/config

/bin/echo "hello world"
runsvdir /etc/service