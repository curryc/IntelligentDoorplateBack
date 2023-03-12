package com.scu.intelligentdoorplateback.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scu.intelligentdoorplateback.common.BaseConstant;
import com.scu.intelligentdoorplateback.mapper.UserMapper;
import com.scu.intelligentdoorplateback.model.domain.Qrcode;
import com.scu.intelligentdoorplateback.mapper.QrcodeMapper;
import com.scu.intelligentdoorplateback.service.QrcodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chen
 * @since 2023-02-28
 */
@Service
public class QrcodeServiceImpl extends ServiceImpl<QrcodeMapper, Qrcode> implements QrcodeService {
    @Autowired
    private QrcodeMapper qrcodeMapper;
    @Override
    public List<Qrcode> queryHouses(Long doorId) {
        QueryWrapper<Qrcode> qrcodeQueryWrapper=new QueryWrapper<>();
        qrcodeQueryWrapper.eq("parent_id",doorId);
//        qrcodeQueryWrapper.eq("type", BaseConstant.INFO_TYPE.get("房屋"));
        List<Qrcode> qrcodeList = qrcodeMapper.selectList(qrcodeQueryWrapper);
        return qrcodeList;
    }
}
