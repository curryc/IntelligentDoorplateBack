package com.scu.intelligentdoorplateback.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scu.intelligentdoorplateback.common.JsonResponse;
import com.scu.intelligentdoorplateback.model.domain.Task;
import com.scu.intelligentdoorplateback.model.vo.UserVO;
import com.scu.intelligentdoorplateback.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/statistic")
public class StatisticController {
    @Autowired
    TaskService taskService;

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @Autowired
    QrcodeService qrcodeService;

    @Autowired
    RecordService recordService;

    @Autowired
    ApplyService applyService;

    /**
     * task统计
     */
    @PostMapping(value = "/taskFromUser")
    @ResponseBody
    public JsonResponse taskFromUser(@RequestBody UserVO user){
//        System.out.println(user.getId());
        LambdaQueryWrapper<Task> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Task::getUserId, user.getId());
        Page<Task> page=new Page<Task>(user.getPageNo(), user.getPageSize());
        IPage ret = taskService.page(page, wrapper);
        return JsonResponse.success(ret);
    }

    @GetMapping(value = "admin")
    @ResponseBody
    public JsonResponse admin(){
        Map<String,Integer> ret = new HashMap<>();
        ret.put("user",userService.count());
        ret.put("apply",applyService.count());
        ret.put("task",taskService.count());
        ret.put("message",messageService.count());
        ret.put("qrcode",qrcodeService.count());
        ret.put("record",recordService.count());
        JsonResponse json = JsonResponse.message(true,"success");
        json.setOtherData(ret);
        return json;
    }
}
