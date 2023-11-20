#!/bin/bash


function buildProjects () {
    echo assembling...
    ./gradlew :common:assemble
    ./gradlew :stravaStore:assemble
    ./gradlew :stravaFetch:assemble
    ./gradlew :stravaView:assemble
}

function composeDown() {
  echo decomposing...
  docker-compose down
}

function composeUp() {
  echo composing...
  docker-compose up -d
}

function __run__() {
  composeDown
  buildProjects
  composeUp $@
}

__run__ $@
