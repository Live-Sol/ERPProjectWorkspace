import os
from fastapi import FastAPI
from pydantic import BaseModel
from dotenv import load_dotenv
from langchain_google_genai import ChatGoogleGenerativeAI
from langchain_core.prompts import PromptTemplate

load_dotenv()

app = FastAPI()
llm = ChatGoogleGenerativeAI(model="gemini-2.5-flash", temperature=0) 
# temperature=0: 창의성 0. 무조건 정확한 답만 내놓으라는 뜻.

# 1. 우리 DB 구조를 AI에게 미리 학습시킴 (Context)
DB_SCHEMA = """
너는 Oracle SQL 전문가야. 아래 테이블 구조를 보고 사용자의 질문을 SQL 쿼리로 변환해줘.

[테이블 정보]
1. Z_MARA (자재 마스터)
 - MATNR (자재번호, PK)
 - MAKTX (자재명)
 - NETPR (단가)
 - STOCK (재고량)

2. Z_VBAK (주문 헤더)
 - VBELN (주문번호, PK)
 - BSTNK (고객명)
 - AUDAT (주문일자)

3. Z_VBAP (주문 상세)
 - VBELN (주문번호, PK)
 - POSNR (품목번호, PK)
 - MATNR (자재번호)
 - KWMENG (주문수량)

[규칙]
1. 오직 SQL 쿼리문만 출력해. (설명, Markdown, ```sql 태그 절대 금지)
2. Oracle 문법을 따라줘.
3. 세미콜론(;)은 빼고 출력해.
"""

class Question(BaseModel):
    content: str

@app.post("/ask")
def ask_ai(question: Question):
    print(f"질문 수신: {question.content}")
    
    # 2. 프롬프트 조립
    prompt = f"{DB_SCHEMA}\n\n사용자 질문: {question.content}\nSQL Query:"
    
    try:
        response = llm.invoke(prompt)
        sql_query = response.content.strip() # 공백 제거
        
        # 혹시 모를 마크다운 제거 (Gemini가 친절해서 가끔 ```sql 을 붙임)
        sql_query = sql_query.replace("```sql", "").replace("```", "").strip()
        
        print(f"생성된 SQL: {sql_query}")
        return {"answer": sql_query} # SQL을 'answer'에 담아서 줌
        
    except Exception as e:
        print(f"에러: {e}")
        return {"answer": "ERROR"}
    

