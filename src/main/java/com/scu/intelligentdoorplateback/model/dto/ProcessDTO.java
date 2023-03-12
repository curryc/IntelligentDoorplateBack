package com.scu.intelligentdoorplateback.model.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class ProcessDTO {
    // 返回的审核状态、取证时间、取证地点
    private Integer verifyStatus;
    private LocalDateTime resTime;
    private String resLocation;
    // 用户身份证
    private String idNumber;
}