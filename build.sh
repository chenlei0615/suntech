#!/usr/bin/env bash

set -eo pipefail
#mvn clean compile package -Pdev -DskipTests=true
modules=( module-api )

for module in "${modules[@]}"; do
    docker build -t "suntech/${module}:latest" ${module}
done

