#!/bin/sh
# Maven wrapper script

if [ -z "$MAVEN_HOME" ] ; then
  echo "MAVEN_HOME is not set"
  exit 1
fi

"$MAVEN_HOME/bin/mvn" "$@"

