package com.scu.intelligentdoorplateback.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scu.intelligentdoorplateback.common.BaseConstant;
import com.scu.intelligentdoorplateback.common.JsonResponse;
import com.scu.intelligentdoorplateback.model.domain.Qrcode;
import com.scu.intelligentdoorplateback.model.domain.Record;
import com.scu.intelligentdoorplateback.model.domain.User;
import com.scu.intelligentdoorplateback.model.dto.PageDTO;
import com.scu.intelligentdoorplateback.model.vo.QrCodeVO;
import com.scu.intelligentdoorplateback.service.QrcodeService;
import com.scu.intelligentdoorplateback.service.RecordService;
import com.scu.intelligentdoorplateback.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


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
@RequestMapping("/api/qrcode")
public class QrcodeController {

    private final Logger logger = LoggerFactory.getLogger( QrcodeController.class );

    @Autowired
    private QrcodeService qrcodeService;
    @Autowired
    private RecordService recordService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse listPage(PageDTO pageDTO){
        Page<Qrcode> page = new Page<>(pageDTO.getPageNo(), pageDTO.getPageSize());
        qrcodeService.page(page);
        return JsonResponse.success(page);
    }
    /**
     * 描述：门牌二维码关联
     *
     */
    @RequestMapping(value = "relateDoorplate",method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse relateDoorplate(@RequestBody Qrcode qrcode){
        System.out.println(qrcode);
        Qrcode qrcode1=qrcodeService.getById(qrcode.getId());
        qrcode1.setPictureUrl(qrcode.getPictureUrl());
        qrcode1.setDocumentUrl(qrcode.getDocumentUrl());
        boolean b = qrcodeService.updateById(qrcode1);
        return JsonResponse.message(b,"更新");
    }
    /**
     * 描述：民警查看楼栋租住信息,返回房屋信息和user的list数组
     *
     */
    @RequestMapping(value = "queryHouse",method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse queryHouse(Long doorId){
        //创建一个qrCodeVO对象
        QrCodeVO qrCodeVO=new QrCodeVO();

        Qrcode qrcode = qrcodeService.getById(doorId);

        qrCodeVO.setId(qrcode.getId());
        qrCodeVO.setUserId(qrcode.getUserId());
        qrCodeVO.setAddress(qrcode.getAddress());
        qrCodeVO.setLongitude(qrcode.getLongitude());
        qrCodeVO.setLatitude(qrcode.getLatitude());
        qrCodeVO.setIsBound(qrcode.getIsBound());
        qrCodeVO.setIsRented(qrcode.getIsRented());
        qrCodeVO.setPictureUrl(qrcode.getPictureUrl());
        qrCodeVO.setDocumentUrl(qrcode.getDocumentUrl());
        qrCodeVO.setIsChecked(qrcode.getIsChecked());
        qrCodeVO.setType(qrcode.getType());
        qrCodeVO.setParentId(qrcode.getParentId());

        List<User> users = new ArrayList<>();

        User householdUser = userService.getById(qrcode.getUserId());
        users.add(householdUser);
        Record record=recordService.getByQrId(doorId);
        if(record!=null){
            User tenantUser=userService.getById(record.getTenantId());
            users.add(tenantUser);
            qrCodeVO.setUsers(users);
        }else {
            qrCodeVO.setUsers(users);
        }
        return JsonResponse.success(qrCodeVO);
    }

    /**
     * 描述：民警查看小区租住信息
     *
     */
    @RequestMapping(value = "queryEstate",method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse queryEstate(Long doorId){
        List<Qrcode> qrcodeList=qrcodeService.queryHouses(doorId);
        return JsonResponse.success(qrcodeList);
    }

//    @RequestMapping(value = "relateDoorplate",method = RequestMethod.POST)
//    @ResponseBody
//    public JsonResponse relateDoorplate()
    /**
    * 描述：根据Id 查询
    *
    */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getById(@PathVariable("id") Long id)throws Exception {
        Qrcode  qrcode =  qrcodeService.getById(id);
        return JsonResponse.success(qrcode);
    }

    @RequestMapping(value = "/getPolice/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getPoliceById(@PathVariable("id") Long id)throws Exception {
        Qrcode  qrcode =  qrcodeService.getById(id);

        QueryWrapper<User> wrapper = new QueryWrapper();
        wrapper.lambda().le(User::getLatitude,qrcode.getLatitude() + 0.05)
                .le(User::getLongitude,qrcode.getLongitude() + 0.05)
                .ge(User::getLatitude,qrcode.getLatitude() - 0.05)
                .ge(User::getLongitude,qrcode.getLongitude() - 0.05)
                .eq(User::getRoleId, BaseConstant.ROLE_MAP.get("民警"));
        return JsonResponse.success(userService.getOne(wrapper));
    }
    /**
    * 描述：根据Id删除
    *
    */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public JsonResponse deleteById(@PathVariable("id") Long id) throws Exception {
        qrcodeService.removeById(id);
        return JsonResponse.success(null);
    }


    /**
    * 描述：根据Id 更新
    *
    */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse updateQrcode(@RequestBody Qrcode  qrcode) throws Exception {
        qrcodeService.updateById(qrcode);
        return JsonResponse.success(null);
    }


    /**
    * 描述:创建Qrcode
    *
    */
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse create(@RequestBody Qrcode  qrcode) throws Exception {
        qrcodeService.save(qrcode);
        return JsonResponse.success(null);
    }

    /**
     * 描述：门牌二维码关联
     */
    @GetMapping(value = "/qrcoderelate")
    @ResponseBody
    @Deprecated
    public JsonResponse qrcoderelate(Long qrId){
        Qrcode qrcode = qrcodeService.getById(qrId);
        return JsonResponse.success(qrcode);
    }
}

