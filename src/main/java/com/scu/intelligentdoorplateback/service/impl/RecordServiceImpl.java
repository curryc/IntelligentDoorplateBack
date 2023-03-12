package com.scu.intelligentdoorplateback.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scu.intelligentdoorplateback.mapper.RecordMapper;
import com.scu.intelligentdoorplateback.model.domain.Record;
import com.scu.intelligentdoorplateback.service.RecordService;
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
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {
    @Autowired
    RecordMapper recordMapper;
    @Override
    public Record getByQrId(Long doorId) {
        QueryWrapper<Record> recordQueryWrapper=new QueryWrapper<>();
        recordQueryWrapper.eq("qr_id",doorId);
        List<Record> records = recordMapper.selectList(recordQueryWrapper);
        for(Record r:records){
            if(r.getIsRented()){
                return r;
            }
        }
        return null;
    }

    @Override
    public List<Record> getRecords(Long userId) {
//        List<RecordVO> records = recordMapper.getRecordWithOwner(userId);
     return null;
    }
}
