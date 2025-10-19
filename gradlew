#!/usr/bin/env sh

DIR="$(cd "$(dirname "$0")" && pwd)"
JAVA_HOME="${JAVA_HOME:-$(/usr/libexec/java_home 2>/dev/null)}"
if [ -z "$JAVA_HOME" ]; then
  JAVA_CMD="java"
else
  JAVA_CMD="$JAVA_HOME/bin/java"
fi

exec "$JAVA_CMD" -Xmx64m -Xms64m -classpath "$DIR/gradle/wrapper/gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain "$@"
