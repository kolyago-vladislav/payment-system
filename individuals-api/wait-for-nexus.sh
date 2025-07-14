#!/bin/bash
set -e

echo "Waiting for Nexus to be ready..."

until curl -sSf http://nexus:8081/repository/maven-snapshots/ > /dev/null; do
  echo "Nexus not ready, waiting..."
  sleep 3
done

echo "Nexus is up, continuing build..."