@echo off
:: 한글 깨짐 방지 (UTF-8 강제 설정)
chcp 65001
cls

echo ==============================================
echo  [보조 시스템 가동] AI 서버 & 프론트엔드
echo ==============================================

:: 1. AI 서버 (Python) 시작
echo [1/2] Python AI Server (FastAPI) 시작...
start "AI Server (Python)" cmd /k "cd ai-server && venv\Scripts\activate && python -m uvicorn main:app --reload"

:: 2. React (Frontend) 시작
echo [2/2] React Frontend 시작...
start "React Frontend" cmd /k "cd frontend && npm run dev"

echo.
echo ==============================================
echo  이제 IntelliJ에서 Java 서버를 실행해주세요!
echo ==============================================
pause