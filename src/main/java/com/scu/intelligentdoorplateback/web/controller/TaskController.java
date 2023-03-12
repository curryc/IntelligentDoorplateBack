package com.scu.intelligentdoorplateback.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scu.intelligentdoorplateback.common.JsonResponse;
import com.scu.intelligentdoorplateback.model.domain.Qrcode;
import com.scu.intelligentdoorplateback.model.domain.Task;
import com.scu.intelligentdoorplateback.model.domain.User;
import com.scu.intelligentdoorplateback.model.dto.LocationDTO;
import com.scu.intelligentdoorplateback.model.dto.PageDTO;
import com.scu.intelligentdoorplateback.model.vo.TaskQrcodeVO;
import com.scu.intelligentdoorplateback.model.vo.UserVO;
import com.scu.intelligentdoorplateback.service.QrcodeService;
import com.scu.intelligentdoorplateback.service.TaskService;
import com.scu.intelligentdoorplateback.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;


/**
 *
 *  前端控制器
 *
 *
 * @author chen
 * @since 2023-02-28
 * @version v1.0
 */
@Controller
@RequestMapping("/api/task")
public class TaskController {

    private final Logger logger = LoggerFactory.getLogger( TaskController.class );

    @Autowired
    private TaskService taskService;

    @Autowired
    private QrcodeService qrcodeService;

    @Autowired
    private UserService userService;

    /**
    * 描述：根据Id 查询
    *
    */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getById(@PathVariable("id") Long id)throws Exception {
        Task  task =  taskService.getById(id);
        return JsonResponse.success(task);
    }

    /**
     * 描述：根据任务Id 查询 TaskQrcodeVO
     *
     */
    @RequestMapping(value = "getHouseInfo",method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getHouseInfo(@RequestParam Long id){
        TaskQrcodeVO taskQrcodeVO=new TaskQrcodeVO();
        //根据任务id获得任务
        Task task = taskService.getById(id);
        if(task!=null){
            //根据qr_id获得
            Qrcode qrcode = qrcodeService.getById(task.getQrId());
            //获得user信息
            User user = userService.getById(qrcode.getUserId());
            taskQrcodeVO.setId(task.getId())
                    .setQrId(task.getQrId())
                    .setUserId(task.getUserId())
                    .setName(task.getName())
                    .setType(task.getType())
                    .setIsCompleted(task.getIsCompleted())
                    .setStartTime(task.getStartTime())
                    .setEndTime(task.getEndTime())
                    .setCompleteTime(task.getCompleteTime())
                    .setCount(task.getCount())
                    .setDescription(task.getDescription());
            taskQrcodeVO.setLongitude(qrcode.getLongitude())
                    .setLatitude(qrcode.getLatitude())
                    .setAddress(qrcode.getAddress())
                    .setRented(qrcode.getIsRented())
                    .setHost(user);
            return JsonResponse.success(taskQrcodeVO);
        }else {
            return JsonResponse.message(false,"查询不到该任务");
        }
    }


    /**
    * 描述：根据Id删除
    *
    */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public JsonResponse deleteById(@PathVariable("id") Long id) throws Exception {
        taskService.removeById(id);
        return JsonResponse.success(null);
    }


    /**
    * 描述：根据Id 更新
    *
    */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse updateTask(@RequestBody Task  task) throws Exception {
        taskService.updateById(task);
        return JsonResponse.success(null);
    }


    /**
    * 描述:创建Task
    *
    */
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse create(@RequestBody Task  task) throws Exception {
        taskService.save(task);
        return JsonResponse.success(null);
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse listPage(PageDTO pageDTO){
        Page<Task> page = new Page<>(pageDTO.getPageNo(), pageDTO.getPageSize());
        taskService.page(page);
        return JsonResponse.success(page);
    }

    /**
     * task统计
     */
    @PostMapping(value = "/statistic")
    @ResponseBody
    @Deprecated
    public JsonResponse statistic(@RequestBody UserVO user){
//        System.out.println(user.getId());
        LambdaQueryWrapper<Task> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Task::getUserId, user.getId());
        Page<Task> page=new Page<Task>(user.getPageNo(), user.getPageSize());
        IPage ret = taskService.page(page, wrapper);
        return JsonResponse.success(ret);
    }

    @PostMapping(value = "/distribute")
    @ResponseBody
    public JsonResponse distribute(@RequestBody LocationDTO locationDTO){
        Page<Task> page = taskService.distributeTask(locationDTO);
        // 标记任务的完成者
        taskService.updateBatchById(page.getRecords().stream().map(t -> t.setUserId(locationDTO.getProcessor_id())).collect(Collectors.toList()));
        return JsonResponse.success(page);
    }

    @GetMapping(value = "getByDoor")
    @ResponseBody
    public JsonResponse getByDoor(@RequestParam("doorId")String doorId){
        Long door = Long.parseLong(doorId);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("qr_id",door);
        wrapper.eq("is_completed",0);
        Task task = taskService.getOne(wrapper);
        if(task == null){
            return JsonResponse.message(false, "nothing");
        }
        return JsonResponse.success(task);
    }
}

