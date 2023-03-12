package com.scu.intelligentdoorplateback.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scu.intelligentdoorplateback.model.domain.Task;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scu.intelligentdoorplateback.model.dto.LocationDTO;
import com.scu.intelligentdoorplateback.model.dto.MatrixDTO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author chen
 * @since 2023-02-28
 */
public interface TaskMapper extends BaseMapper<Task> {
    Page<Task> pageLocation(Page<Task> page, @Param("matrixDTO") MatrixDTO matrixDTO);
}
