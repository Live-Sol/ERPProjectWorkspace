@echo off
:: 한글 깨짐 방지 (UTF-8 강제 설정)
chcp 65001
cls

echo ==============================================
echo  Intelli-ERP 시스템을 모두 종료합니다...
echo ==============================================

:: 1. Java 종료 (8080 포트 사용하는 프로세스 사살)
echo [1/3] Java Spring Boot 종료 중...
for /f "tokens=5" %%a in ('netstat -aon ^| find ":8080" ^| find "LISTENING"') do taskkill /f /pid %%a >nul 2>&1

:: 2. Python 종료 (8000 포트 사용하는 프로세스 사살)
echo [2/3] Python AI Server 종료 중...
for /f "tokens=5" %%a in ('netstat -aon ^| find ":8000" ^| find "LISTENING"') do taskkill /f /pid %%a >nul 2>&1

:: 3. React 종료 (Node.js 프로세스 사살)
echo [3/3] React Frontend 종료 중...
taskkill /f /im node.exe >nul 2>&1

echo.
echo 모든 서버가 깔끔하게 종료되었습니다.
pause