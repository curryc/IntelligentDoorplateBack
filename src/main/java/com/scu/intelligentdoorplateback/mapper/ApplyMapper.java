package com.scu.intelligentdoorplateback.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scu.intelligentdoorplateback.model.domain.Apply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scu.intelligentdoorplateback.model.vo.ApplyWithApplicant;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author chen
 * @since 2023-03-01
 */
public interface ApplyMapper extends BaseMapper<Apply> {

    Page<ApplyWithApplicant> selectApplyWithApplicant(@Param("page") Page<ApplyWithApplicant> page, @Param("id") Long id);
}
