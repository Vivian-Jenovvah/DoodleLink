@rem Minimal standard Gradle Wrapper launcher for Windows.
@echo off
setlocal
set APP_HOME=%~dp0
if "%JAVA_HOME%"=="" (
  echo ERROR: JAVA_HOME must point to a JDK 21 installation.
  exit /b 1
)
"%JAVA_HOME%\bin\java.exe" -classpath "%APP_HOME%gradle\wrapper\gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain %*
exit /b %ERRORLEVEL%
