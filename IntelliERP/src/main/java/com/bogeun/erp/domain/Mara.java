package com.bogeun.erp.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity                   // 이 클래스는 DB 테이블과 매핑된다는 표시
@Table(name = "Z_MARA")   // 오라클의 'Z_MARA' 테이블과 연결하겠다 (이거 안 쓰면 'Mara' 테이블 찾음)
@Getter @Setter           // Lombok: 지루한 get/set 메소드 자동 생성
public class Mara {

    @Id
    @Column(name = "MATNR")
    private String matnr; // 자재 번호

    @Column(name = "MAKTX", nullable = false)
    private String maktx; // 자재 명세

    @Column(name = "MATKL")
    private String matkl; // 자재 그룹

    @Column(name = "NETPR")
    private Long netpr;   // 단가 (오라클 Number -> 자바 Long/Double)

    @Column(name = "STOCK")
    private Integer stock; // 재고
}