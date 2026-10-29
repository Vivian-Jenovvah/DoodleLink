#!/usr/bin/env sh
APP_HOME=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
if [ -z "$JAVA_HOME" ]; then
  echo "ERROR: JAVA_HOME must point to a JDK 21 installation." >&2
  exit 1
fi
exec "$JAVA_HOME/bin/java" -classpath "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain "$@"
