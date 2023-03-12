package com.scu.intelligentdoorplateback.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scu.intelligentdoorplateback.common.BaseConstant;
import com.scu.intelligentdoorplateback.model.domain.Apply;
import com.scu.intelligentdoorplateback.mapper.ApplyMapper;
import com.scu.intelligentdoorplateback.model.vo.ApplyWithApplicant;
import com.scu.intelligentdoorplateback.service.ApplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chen
 * @since 2023-03-01
 */
@Service
public class ApplyServiceImpl extends ServiceImpl<ApplyMapper, Apply> implements ApplyService {

    @Autowired
    private ApplyMapper applyMapper;

    @Override
    public void ById(Apply apply) {

    }

    @Override
    public List<Apply> getUserApply(Long processorId) {
        QueryWrapper<Apply> applyQueryWrapper = new QueryWrapper<>();
        applyQueryWrapper.eq("processor_id", processorId);
        applyQueryWrapper.eq("verify_status",BaseConstant.VERIFY_STATUS.get("已提交"));
        return applyMapper.selectList(applyQueryWrapper);
    }

    @Override
    public Apply findById(Long id) {
        QueryWrapper<Apply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        return baseMapper.selectOne(queryWrapper);
    }


    @Override
    @Deprecated
    public Page<ApplyWithApplicant> getApplyAndApplicant(Long id) {
        Page<ApplyWithApplicant> page = new Page<>(0, Integer.MAX_VALUE);
        applyMapper.selectApplyWithApplicant(page, id);
        return page;
    }

    @Override
    public List<Apply> getApplies(Long userId) {
        QueryWrapper<Apply> applyQueryWrapper=new QueryWrapper<>();
        applyQueryWrapper.eq("user_id",userId);
        applyQueryWrapper.eq("type",BaseConstant.APPLY_TYPE.get("居住证"));
        applyQueryWrapper.orderByDesc("apply_time");
        return applyMapper.selectList(applyQueryWrapper);
    }
}
