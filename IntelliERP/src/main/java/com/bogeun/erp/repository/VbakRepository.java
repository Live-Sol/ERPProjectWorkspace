package com.bogeun.erp.repository;

import com.bogeun.erp.domain.Vbak;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VbakRepository extends JpaRepository<Vbak, String> {
    // 특정 고객(BSTNK)이 주문한 내역 찾기
    List<Vbak> findByBstnk(String customerName);
}