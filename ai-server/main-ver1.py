from fastapi import FastAPI
from pydantic import BaseModel

app = FastAPI()

# 데이터 주고받을 그릇 (DTO와 비슷)
class Question(BaseModel):
    content: str

@app.get("/")
def read_root():
    return {"status": "AI Server is Running", "message": "Java 친구야 안녕?"}

# Java가 질문을 던지면 받아줄 곳
@app.post("/ask")
def ask_ai(question: Question):
    print(f"Java가 보낸 질문: {question.content}")
    
    # 여기에 나중에 LangChain 로직이 들어갈 예정!
    dummy_answer = f"AI가 분석한 결과: '{question.content}'에 대한 답변입니다."
    
    return {"answer": dummy_answer}