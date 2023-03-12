package com.scu.intelligentdoorplateback.model.vo;

import com.scu.intelligentdoorplateback.model.domain.Task;
import com.scu.intelligentdoorplateback.model.domain.User;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TaskQrcodeVO extends Task {
    private String address;

    private Double longitude;

    private Double latitude;

    private Boolean rented;

    private User host;

}
