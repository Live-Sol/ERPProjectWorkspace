package com.bogeun.erp.controller;

import com.bogeun.erp.domain.Mara;
import com.bogeun.erp.repository.MaraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController // "나는 화면(HTML) 대신 데이터(JSON)를 주는 사람이야"
@RequestMapping("/api/v1/materials") // 이 주소로 오면 내가 처리할게
@RequiredArgsConstructor
public class MaraController {

    private final MaraRepository maraRepository;

    // 조회 (GET)
    // 주소: http://localhost:8080/api/v1/materials
    @GetMapping
    public List<Mara> getAllMaterials() {
        return maraRepository.findAll(); // 자바 객체 리스트를 주면, Spring이 알아서 JSON으로 바꿔줍니다.
    }
}