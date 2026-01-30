package com.bogeun.erp.repository;

import com.bogeun.erp.domain.Mara;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Repository기본 기능: save(), findAll(), findById() 등은 안 적어도 공짜로 제공됨!
@Repository
public interface MaraRepository extends JpaRepository<Mara, String> {
    // findBy쿼리 메소드 (Magic Method)
    // "이름(MAKTX)에 특정 단어가 포함된 자재를 찾아줘" -> SQL 없이 메소드 이름만 잘 지으면 끝!
    List<Mara> findByMaktxContaining(String keyword);
}