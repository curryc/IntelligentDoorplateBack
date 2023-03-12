package com.scu.intelligentdoorplateback.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scu.intelligentdoorplateback.model.domain.Task;
import com.scu.intelligentdoorplateback.mapper.TaskMapper;
import com.scu.intelligentdoorplateback.model.dto.LocationDTO;
import com.scu.intelligentdoorplateback.model.dto.MatrixDTO;
import com.scu.intelligentdoorplateback.service.TaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chen
 * @since 2023-02-28
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {
    @Autowired
    TaskMapper taskMapper;

    @Override
    public Page<Task> distributeTask(LocationDTO locationDTO) {
        Page<Task> page = new Page<>(locationDTO.getPageNo(), locationDTO.getPageSize());
        MatrixDTO matrix = new MatrixDTO(locationDTO.getLongitude() - 0.05,
                locationDTO.getLatitude() + 0.05,
                locationDTO.getLongitude() + 0.05,
                locationDTO.getLatitude() - 0.05);
        taskMapper.pageLocation(page, matrix);
        return page;
    }
}
