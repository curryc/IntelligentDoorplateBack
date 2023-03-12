package com.scu.intelligentdoorplateback.service;

import com.scu.intelligentdoorplateback.model.domain.Qrcode;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chen
 * @since 2023-02-28
 */
public interface QrcodeService extends IService<Qrcode> {
    List<Qrcode> queryHouses(Long doorId);
}
