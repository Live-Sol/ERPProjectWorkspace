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

# 1.DB 구조를 AI에게 미리 학습시킴 (Context)
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

# 2. 수신할 질문의 데이터 구조Question
class Question(BaseModel):
    content: str

# 3. 수리 요청을 받을 데이터 구조FixRequest
class FixRequest(BaseModel):
    question: str      # 원래 질문
    wrong_sql: str     # 틀린 SQL
    error_msg: str     # 발생한 에러 메시지

# 4. 질문 수신 엔드포인트
@app.post("/ask")
def ask_ai(question: Question):
    print(f"질문 수신: {question.content}")
    
    # 프롬프트 조립
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
    
# 5. SQL 수정 요청 엔드포인트
@app.post("/fix")
def fix_sql(req: FixRequest):
    print(f"🚨 SQL 수정 요청 들어옴!\n- 원본질문: {req.question}\n- 틀린쿼리: {req.wrong_sql}\n- 에러내용: {req.error_msg}")

    # 수리용 프롬프트 (틀렸다고 알려주고, 에러 메시지를 단서로 제공합니다.)
    fix_prompt = f"""
    {DB_SCHEMA}
    
    [상황]
    사용자 질문: "{req.question}"
    네가 만든 SQL: "{req.wrong_sql}"
    발생한 에러: "{req.error_msg}"
    
    [지시]
    위 에러 메시지를 보고 SQL을 올바르게 수정해줘.
    
    ★ 중요 규칙 ★
    1. 에러가 'invalid identifier(없는 컬럼)'라면, 억지로 다른 컬럼을 별칭(AS)으로 가려서 거짓말하지 마.
    2. 차라리 존재하는 가장 관련성 높은 컬럼(예: 자재명, 스펙)을 있는 그대로 조회해.
    3. 설명 없이 오직 SQL만 출력해.
    """

    # 수리 요청
    try:
        response = llm.invoke(fix_prompt)
        fixed_sql = response.content.strip()
        fixed_sql = fixed_sql.replace("```sql", "").replace("```", "").strip()
        
        print(f"✅ 수정된 SQL: {fixed_sql}")
        return {"answer": fixed_sql}
        
    except Exception as e:
        print(f"수정 실패: {e}")
        return {"answer": "ERROR"}

# python -m uvicorn main:app --reload