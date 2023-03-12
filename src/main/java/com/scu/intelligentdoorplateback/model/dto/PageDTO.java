package com.scu.intelligentdoorplateback.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PageDTO {
    private Integer pageNo = 1;
    private Integer pageSize = 10;
}

