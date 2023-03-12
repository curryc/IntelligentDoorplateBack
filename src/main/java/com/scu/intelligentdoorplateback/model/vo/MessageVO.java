package com.scu.intelligentdoorplateback.model.vo;

import com.scu.intelligentdoorplateback.model.domain.Message;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MessageVO extends Message {
    // 当前的操作者
    private Long processorId;
    // 当前门牌id，扫码填message自然获得
    private Long doorId;
    //当前房屋是否正在出租,用于核对信息
    private Integer rented;
}
