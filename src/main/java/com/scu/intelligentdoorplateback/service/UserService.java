package com.scu.intelligentdoorplateback.service;

import com.scu.intelligentdoorplateback.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chen
 * @since 2023-02-28
 */
public interface UserService extends IService<User> {

    void ById(User user);

}
