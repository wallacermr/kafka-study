#!/bin/sh
set -e

echo;echo "### Parando Imagens Docker... ###";echo
docker-compose -f docker-compose.yml down

echo;echo "### Limpando volumes... ###";echo
docker volume rm docker-local_zookeeper-dev \
                 docker-local_kafka-dev

docker volume prune -f