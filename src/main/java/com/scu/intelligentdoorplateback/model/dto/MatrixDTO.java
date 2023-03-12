package com.scu.intelligentdoorplateback.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MatrixDTO {
    private Double left;
    private Double top;
    private Double right;
    private Double bottom;

    public MatrixDTO(Double left, Double top, Double right, Double bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }
}
