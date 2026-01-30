package com.bogeun.erp.controller;

import com.bogeun.erp.domain.Vbak;
import com.bogeun.erp.domain.Vbap;
import com.bogeun.erp.repository.VbakRepository;
import com.bogeun.erp.repository.VbapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final VbakRepository vbakRepository;
    private final VbapRepository vbapRepository;

    // 1. 전체 주문 목록 보기
    // 주소: http://localhost:8080/api/v1/orders
    @GetMapping
    public List<Vbak> getAllOrders() {
        return vbakRepository.findAll();
    }

    // 2. 특정 주문의 상세 품목 보기 (동적 파라미터)
    // 주소: http://localhost:8080/api/v1/orders/{주문번호}/items
    // 예시: http://localhost:8080/api/v1/orders/ORD-20240126-01/items
    @GetMapping("/{vbeln}/items")
    public List<Vbap> getOrderItems(@PathVariable String vbeln) {
        return vbapRepository.findByVbeln(vbeln);
    }
}