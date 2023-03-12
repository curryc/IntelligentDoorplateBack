package com.scu.intelligentdoorplateback.service;

import com.scu.intelligentdoorplateback.model.domain.Record;
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
public interface RecordService extends IService<Record> {
    List<Record> getRecords(Long userId);

    Record getByQrId(Long doorId);
}
