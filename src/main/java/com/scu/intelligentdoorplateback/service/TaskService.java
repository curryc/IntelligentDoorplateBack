package com.scu.intelligentdoorplateback.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scu.intelligentdoorplateback.model.domain.Task;
import com.baomidou.mybatisplus.extension.service.IService;
import com.scu.intelligentdoorplateback.model.dto.LocationDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chen
 * @since 2023-02-28
 */
public interface TaskService extends IService<Task> {

    Page<Task> distributeTask(LocationDTO locationDTO);
}
