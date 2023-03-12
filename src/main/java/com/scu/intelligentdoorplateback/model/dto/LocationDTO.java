package com.scu.intelligentdoorplateback.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LocationDTO extends PageDTO{
    private Double longitude;
    private Double latitude;

    // 当前的操作者
    private Long processor_id;
}
