package com.scu.intelligentdoorplateback.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scu.intelligentdoorplateback.model.domain.Record;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author chen
 * @since 2023-02-28
 */
public interface RecordMapper extends BaseMapper<Record> {
    List<Record> getRecordWithOwner(@Param("userid") Long userid);
}
