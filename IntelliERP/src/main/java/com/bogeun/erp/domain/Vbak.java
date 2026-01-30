package com.bogeun.erp.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "Z_VBAK")
@Getter @Setter
public class Vbak {

    @Id
    @Column(name = "VBELN")
    private String vbeln; // 판매 문서 번호

    @Column(name = "BSTNK")
    private String bstnk; // 고객 참조

    @Column(name = "AUDAT")
    private LocalDate audat; // 주문 일자 (자바 8부터는 Date 대신 LocalDate 씁니다! 훨씬 편해요)

    @Column(name = "ERNAM")
    private String ernam; // 생성자
}