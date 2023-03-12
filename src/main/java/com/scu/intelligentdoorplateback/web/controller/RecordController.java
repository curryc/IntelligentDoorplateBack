package com.scu.intelligentdoorplateback.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scu.intelligentdoorplateback.common.BaseConstant;
import com.scu.intelligentdoorplateback.common.JsonResponse;
import com.scu.intelligentdoorplateback.model.domain.Apply;
import com.scu.intelligentdoorplateback.model.domain.Record;
import com.scu.intelligentdoorplateback.model.dto.PageDTO;
import com.scu.intelligentdoorplateback.service.ApplyService;
import com.scu.intelligentdoorplateback.service.QrcodeService;
import com.scu.intelligentdoorplateback.service.RecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
@RequestMapping("/api/record")
public class RecordController {

    private final Logger logger = LoggerFactory.getLogger( RecordController.class );

    @Autowired
    private RecordService recordService;
    @Autowired
    private QrcodeService qrcodeService;
    @Autowired
    private ApplyService applyService;

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse listPage(PageDTO pageDTO){
        Page<Record> page = new Page<>(pageDTO.getPageNo(), pageDTO.getPageSize());
        recordService.page(page);
        return JsonResponse.success(page);
    }

    /**
    * 描述：根据Id 查询
    *
    */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getById(@PathVariable("id") Long id)throws Exception {
        Record  record =  recordService.getById(id);
        return JsonResponse.success(record);
    }

    /**
    * 描述：根据Id删除
    *
    */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public JsonResponse deleteById(@PathVariable("id") Long id) throws Exception {
        recordService.removeById(id);
        return JsonResponse.success(null);
    }


    /**
    * 描述：根据Id 更新
    *
    */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse updateRecord(@RequestBody Record  record) throws Exception {
        recordService.updateById(record);
        return JsonResponse.success(null);
    }


    /**
    * 描述:创建Record
    *
    */
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse create(@RequestBody Record  record) throws Exception {
        recordService.save(record);
        return JsonResponse.success(null);
    }


    /**
     * 描述：户主出租历史
     */
    @GetMapping(value = "/rentHistory")
    @ResponseBody
    public JsonResponse rentHistory(@RequestParam Long user_id){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("processor_id", user_id);
        wrapper.eq("verify_status", BaseConstant.VERIFY_STATUS.get("审核成功"));
        Page<Apply> ret = applyService.page(new Page<Apply>(0, 1000), wrapper);
        return JsonResponse.success(ret);
    }

    /**
     * 描述：房屋出租历史
     */
    @GetMapping(value = "/doorHistory")
    @ResponseBody
    public JsonResponse doorHistory(@RequestParam Long doorId){
        QueryWrapper<Record> wrapper1 = new QueryWrapper();
        wrapper1.eq("qr_id",doorId);
        List<Long> l1 = recordService.list(wrapper1).stream().map(record -> record.getApplyId()).collect(Collectors.toList());
        QueryWrapper<Apply> wrapper2 = new QueryWrapper();
        wrapper2.lambda().in(Apply::getId,l1);
        List p = applyService.list(wrapper2);

        if (p != null) {
            return JsonResponse.success(p);
        } else {
            return JsonResponse.failure("没有出租记录");
        }
    }
}

