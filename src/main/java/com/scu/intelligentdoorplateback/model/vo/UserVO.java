package com.scu.intelligentdoorplateback.model.vo;

import com.scu.intelligentdoorplateback.model.domain.User;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserVO extends User {
    int pageNo;
    int pageSize;
    Long doorId;
}
