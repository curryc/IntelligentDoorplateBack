package com.scu.intelligentdoorplateback.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scu.intelligentdoorplateback.common.BaseConstant;
import com.scu.intelligentdoorplateback.common.JsonResponse;
import com.scu.intelligentdoorplateback.model.domain.Message;
import com.scu.intelligentdoorplateback.model.domain.Task;
import com.scu.intelligentdoorplateback.model.dto.PageDTO;
import com.scu.intelligentdoorplateback.model.vo.MessageVO;
import com.scu.intelligentdoorplateback.service.MessageService;
import com.scu.intelligentdoorplateback.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


/**
 * 前端控制器
 *
 * @author chen
 * @version v1.0
 * @since 2023-02-28
 */
@Controller
@RequestMapping("/api/message")
public class MessageController {

    private final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private MessageService messageService;

    /**
     * 描述：根据Id 查询
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getById(@PathVariable("id") Long id) throws Exception {
        Message message = messageService.getById(id);
        return JsonResponse.success(message);
    }

    /**
     * 描述：根据Id删除
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public JsonResponse deleteById(@PathVariable("id") Long id) throws Exception {
        messageService.removeById(id);
        return JsonResponse.success(null);
    }


    /**
     * 描述：根据Id 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse updateMessage(@RequestBody Message message) throws Exception {
        messageService.updateById(message);
        return JsonResponse.success(null);
    }


    /**
     * 描述:创建Message
     */
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse create(@RequestBody Message message) throws Exception {
        messageService.save(message);
        return JsonResponse.success(null);
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse listPage(PageDTO pageDTO){
        Page<Message> page = new Page<>(pageDTO.getPageNo(), pageDTO.getPageSize());
        messageService.page(page);
        return JsonResponse.success(page);
    }


    @Autowired
    TaskService taskService;

    /**
     * 巡警工作完成
     */
    @PostMapping(value = "/complete")
    @ResponseBody
    public JsonResponse completeSchedule(@RequestBody MessageVO vo) {
        QueryWrapper wrapper =  new QueryWrapper();
        wrapper.eq("qr_id",vo.getDoorId());
        wrapper.eq("is_completed",0);
        Task task = taskService.getOne(wrapper);
        if(task != null) {
            // 在message表中添加条目
            Message message = new MessageVO();
            message.setUserId(vo.getProcessorId())
                    .setDescription(vo.getDescription())
                    .setLongitude(vo.getLongitude())
                    .setLatitude(vo.getLatitude())
                    .setAddress(vo.getAddress())
                    .setVisitTime(vo.getVisitTime())
                    .setPictureUrl(vo.getPictureUrl())
                    .setTaskId(task.getId());
            messageService.save(message);
            // 在任务列表更新定时任务
            task.setIsCompleted(1)
                    .setCompleteTime(LocalDateTime.now());
            taskService.updateById(task);
            // 如果这个定时任务还需循环一次以上，添加一个定时任务，作为此任务的继承
            if(task.getCount()>1) {
                Task task1 = new Task();
                task1.setQrId(task.getQrId())
                        .setUserId(task.getUserId())
                        .setName(task.getName())
                        .setType(task.getType())
                        .setStartTime(LocalDateTime.now().plusDays(25))
                        .setEndTime(LocalDateTime.now().plusDays(30))
                        .setCount(task.getCount() - 1)
                        .setDescription(task.getDescription());
                taskService.save(task1);
            }
            return JsonResponse.message(true, "complete schedule Task successfully");
        }else{
            // 在任务列表添加临时任务
            task = new Task();
            task.setUserId(vo.getProcessorId())
                    .setIsCompleted(1)
                    .setCompleteTime(vo.getVisitTime())
                    .setQrId(vo.getDoorId())
                    .setType(BaseConstant.TASK_TYPE.get("临时任务"))
                    .setDescription(vo.getDescription());
            taskService.save(task);
            // 在message表中添加条目
            Message message = new MessageVO();
            message.setUserId(vo.getProcessorId())
                    .setDescription(vo.getDescription())
                    .setLongitude(vo.getLongitude())
                    .setLatitude(vo.getLatitude())
                    .setAddress(vo.getAddress())
                    .setVisitTime(LocalDateTime.now())
                    .setPictureUrl(vo.getPictureUrl())
                    .setTaskId(task.getId());
            messageService.save(message);
            return JsonResponse.message(true, "complete tempo Task successfully");
        }
    }
}

