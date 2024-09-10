#!/bin/sh
set -e

cpu_platform=$(uname -p)
file_name="docker-compose.yml"

if [ "$cpu_platform" = "arm" ]; then
    echo "Plataforma ARM"
    file_name="docker-compose.yml"
fi

echo;echo "### Iniciando imagens Docker...";echo
docker-compose -f $file_name down
sleep 10

echo;echo "### Starting docker images... ###";echo
docker-compose -f $file_name --compatibility up --build -d
echo "### Imagens Docker iniciadas com sucesso ###"