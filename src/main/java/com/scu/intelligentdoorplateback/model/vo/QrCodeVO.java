package com.scu.intelligentdoorplateback.model.vo;

import com.scu.intelligentdoorplateback.model.domain.Qrcode;
import com.scu.intelligentdoorplateback.model.domain.User;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)

public class QrCodeVO extends Qrcode{
    private List<User> users;
}
