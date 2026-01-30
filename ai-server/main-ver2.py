import os
from fastapi import FastAPI
from pydantic import BaseModel
from dotenv import load_dotenv

# 1. LangChain에서 구글 Gemini 모듈 가져오기
from langchain_google_genai import ChatGoogleGenerativeAI

# 2. .env 파일에서 API KEY 로드
load_dotenv()

app = FastAPI()

# 3. Gemini 모델 세팅 (가볍고 빠른 2.5-flash 모델 사용 추천)
# API Key는 환경변수에서 자동으로 찾아갑니다.
llm = ChatGoogleGenerativeAI(model="gemini-2.5-flash")

class Question(BaseModel):
    content: str

@app.get("/")
def read_root():
    return {"status": "AI Server is Running", "model": "Gemini 2.5 Flash"}

@app.post("/ask")
def ask_ai(question: Question):
    print(f"Java가 보낸 질문: {question.content}")
    
    # 4. Gemini에게 질문 던지기 (invoke)
    try:
        response = llm.invoke(question.content)
        answer_text = response.content
        print(f"Gemini 답변: {answer_text}")
        
        return {"answer": answer_text}
        
    except Exception as e:
        print(f"에러 발생: {str(e)}")
        return {"answer": "죄송합니다. AI 서버에서 오류가 발생했습니다."}