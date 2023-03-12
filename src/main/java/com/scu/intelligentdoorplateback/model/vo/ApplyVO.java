package com.scu.intelligentdoorplateback.model.vo;

import com.scu.intelligentdoorplateback.model.domain.Apply;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ApplyVO extends Apply {
    private Long doorId;
}
