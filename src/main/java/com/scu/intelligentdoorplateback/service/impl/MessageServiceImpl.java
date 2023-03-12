package com.scu.intelligentdoorplateback.service.impl;

import com.scu.intelligentdoorplateback.model.domain.Message;
import com.scu.intelligentdoorplateback.mapper.MessageMapper;
import com.scu.intelligentdoorplateback.service.MessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

}
