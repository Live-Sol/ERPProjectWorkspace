package com.bogeun.erp.repository;

import com.bogeun.erp.domain.Vbap;
import com.bogeun.erp.domain.VbapId; // 복합키 클래스 import 필수
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VbapRepository extends JpaRepository<Vbap, VbapId> {
    // <Vbap, VbapId> <-- 여기를 String으로 적으면 에러!!
    // 특정 주문번호(VBELN)에 속한 모든 품목 가져오기
    // 복합키의 일부인 vbeln만 가지고도 조회가 가능합니다.
    List<Vbap> findByVbeln(String vbeln);
}