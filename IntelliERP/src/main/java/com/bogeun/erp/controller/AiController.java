package com.bogeun.erp.controller;

import com.bogeun.erp.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    // 기존 단순 질문용 (사용 안 함)
    // @GetMapping("/ask")
    // public String ask(@RequestParam String q) {
    //     return aiService.getAnswerFromAi(q);
    // }

    // 자연어로 DB 조회하기
    // 주소: http://localhost:8080/api/v1/ai/query?q=가격이 5만원 이상인 자재 알려줘
    @GetMapping("/query")
    public List<Map<String, Object>> queryDb(@RequestParam String q) {
        return aiService.executeAiQuery(q);
    }
}