package com.bogeun.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate; // ★ 추가
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiService {

    private final RestTemplate restTemplate; // Python 서버와 통신하는 도구
    private final JdbcTemplate jdbcTemplate; // 날것의 SQL을 실행하는 도구

    private final String AI_SERVER_URL = "http://localhost:8000/ask"; // Python AI 서버 주소
    private final String AI_FIX_URL = "http://localhost:8000/fix"; // 수리 요쳥용 주소 추가

    // 1. Python에게 SQL 받아오기
    public String getSqlFromAi(String questionText) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("content", questionText);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            Map response = restTemplate.postForObject(AI_SERVER_URL, requestEntity, Map.class);
            return (String) response.get("answer");
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    // 2. 받아온 SQL을 진짜 DB에서 실행하기
    public List<Map<String, Object>> executeAiQuery(String question) {
        // 1. 처음 시도
        String sql = getSqlFromAi(question);
        System.out.println("1차 시도 SQL: " + sql);

        try {
            // 실행 시도!
            return jdbcTemplate.queryForList(sql);

        } catch (Exception e) {
            // 2. 에러 발생! (여기서 자가 치유 시작)
            System.err.println("🚨 1차 실행 실패! 에러 원인: " + e.getMessage());
            System.out.println("🩹 자가 치유(Self-Correction)를 시도합니다...");

            // 3. Python에게 수리 요청 보내기
            String fixedSql = getFixedSqlFromAi(question, sql, e.getMessage());
            System.out.println("2차 시도(수정된) SQL:  " + fixedSql);

            // 4. 재실행 (여기서도 안 되면 진짜 에러로 처리)
            return jdbcTemplate.queryForList(fixedSql);
        }
    }

    // 3. Python의 /fix 엔드포인트 호출하는 메소드
    private String getFixedSqlFromAi(String question, String wrongSql, String errorMsg) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("question", question);
        requestBody.put("wrong_sql", wrongSql);
        requestBody.put("error_msg", errorMsg); // 에러 메시지가 너무 길면 Python이 힘들어하므로 앞부분만 자를 수도 있음 (지금은 전체 전송)

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            Map response = restTemplate.postForObject(AI_FIX_URL, requestEntity, Map.class);
            return (String) response.get("answer");
        } catch (Exception e) {
            return "ERROR";
        }
    }

}