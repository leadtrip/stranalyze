#!/usr/bin/env bash

function run() {
  java -jar ./build/libs/stravaCli-0.1-all.jar "${@}"
}

run "${@}"