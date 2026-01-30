package com.bogeun.erp.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Z_VBAP")
@IdClass(VbapId.class) // "제 PK는 VbapId 클래스에 정의되어 있어요" 라고 선언
@Getter @Setter
public class Vbap {

    @Id // 복합키 중 첫 번째
    @Column(name = "VBELN")
    private String vbeln; // 판매 문서 번호

    @Id // 복합키 중 두 번째
    @Column(name = "POSNR")
    private Integer posnr; // 아이템 번호

    @Column(name = "MATNR")
    private String matnr; // 자재 번호

    @Column(name = "KWMENG")
    private Integer kwmeng; // 주문 수량

    @Column(name = "NETWR")
    private Long netwr; // 합계 금액
}