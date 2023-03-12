package com.scu.intelligentdoorplateback.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scu.intelligentdoorplateback.model.domain.Apply;
import com.baomidou.mybatisplus.extension.service.IService;
import com.scu.intelligentdoorplateback.model.vo.ApplyWithApplicant;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chen
 * @since 2023-03-01
 */
public interface ApplyService extends IService<Apply> {

    void ById(Apply apply);

    Apply findById(Long id);

    List<Apply> getUserApply(Long processorId);

    @Deprecated
    Page<ApplyWithApplicant> getApplyAndApplicant(Long id);

    List<Apply> getApplies(Long userId);
}
