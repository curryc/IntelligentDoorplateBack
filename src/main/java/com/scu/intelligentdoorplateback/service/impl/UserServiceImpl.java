package com.scu.intelligentdoorplateback.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scu.intelligentdoorplateback.model.domain.User;
import com.scu.intelligentdoorplateback.mapper.UserMapper;
import com.scu.intelligentdoorplateback.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chen
 * @since 2023-02-28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public void ById(User user) {

    }


}
