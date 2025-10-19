@ECHO OFF
SET DIR=%~dp0
SET JAVA_EXE=%JAVA_HOME%\bin\java.exe
IF NOT EXIST "%JAVA_EXE%" SET JAVA_EXE=java.exe
"%JAVA_EXE%" -Xmx64m -Xms64m -classpath "%DIR%\gradle\wrapper\gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain %*
