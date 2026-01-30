-- 기존에 혹시 있을지 모를 테이블 삭제 (에러나면 무시하세요)
DROP TABLE Z_MARA CASCADE CONSTRAINTS;

-- 자재 마스터 테이블 생성
CREATE TABLE Z_MARA (
    MATNR VARCHAR2(20) PRIMARY KEY, -- 자재 번호 (Material Number)
    MAKTX VARCHAR2(100) NOT NULL,   -- 자재 명세 (Material Description)
    MATKL VARCHAR2(10),             -- 자재 그룹 (Material Group)
    NETPR NUMBER(10, 2),            -- 단가 (Net Price)
    STOCK NUMBER(10) DEFAULT 0      -- 현재 재고 (Stock)
);

-- 더미 데이터 넣기 (테스트용)
INSERT INTO Z_MARA VALUES ('M-001', '고성능 게이밍 마우스', 'ELEC', 50000, 100);
INSERT INTO Z_MARA VALUES ('M-002', '기계식 키보드 (청축)', 'ELEC', 120000, 50);
INSERT INTO Z_MARA VALUES ('M-003', '27인치 모니터', 'ELEC', 250000, 30);
INSERT INTO Z_MARA VALUES ('M-004', '인체공학 의자', 'FURN', 350000, 10);
COMMIT;


-- 테이블 삭제
DROP TABLE Z_VBAP CASCADE CONSTRAINTS;
DROP TABLE Z_VBAK CASCADE CONSTRAINTS;

-- 판매 문서 헤더 (주문서 겉표지)
CREATE TABLE Z_VBAK (
    VBELN VARCHAR2(20) PRIMARY KEY, -- 판매 문서 번호 (Order Number)
    BSTNK VARCHAR2(50),             -- 고객 참조 번호 (Customer Ref)
    AUDAT DATE DEFAULT SYSDATE,     -- 주문 일자 (Document Date)
    ERNAM VARCHAR2(50)              -- 주문 생성자 (Created By)
);

-- 판매 문서 품목 (주문서 내용물)
CREATE TABLE Z_VBAP (
    VBELN VARCHAR2(20),             -- 판매 문서 번호 (FK)
    POSNR NUMBER(6),                -- 품목 번호 (10, 20, 30...)
    MATNR VARCHAR2(20),             -- 자재 번호 (FK to Z_MARA)
    KWMENG NUMBER(10),              -- 주문 수량 (Order Quantity)
    NETWR NUMBER(15, 2),            -- 품목 합계 금액
    CONSTRAINT PK_Z_VBAP PRIMARY KEY (VBELN, POSNR),
    CONSTRAINT FK_Z_VBAP_HEAD FOREIGN KEY (VBELN) REFERENCES Z_VBAK(VBELN),
    CONSTRAINT FK_Z_VBAP_MAT FOREIGN KEY (MATNR) REFERENCES Z_MARA(MATNR)
);

-- 더미 데이터 (주문 1건 생성)
-- 주문서 헤더
INSERT INTO Z_VBAK VALUES ('ORD-20240126-01', 'REF-Samsung', TO_DATE('2024-01-26', 'YYYY-MM-DD'), '조보근');

-- 주문서 내용 (마우스 2개, 키보드 1개 주문)
INSERT INTO Z_VBAP VALUES ('ORD-20240126-01', 10, 'M-001', 2, 100000);
INSERT INTO Z_VBAP VALUES ('ORD-20240126-01', 20, 'M-002', 1, 120000);

COMMIT;