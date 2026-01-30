package com.bogeun.erp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data // Getter, Setter, Equals, HashCode 한번에 해결
@NoArgsConstructor
@AllArgsConstructor
public class VbapId implements Serializable {
    // 복합키를 구성하는 컬럼들을 변수로 선언
    private String vbeln;
    private Integer posnr;
}