package com.scu.intelligentdoorplateback.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scu.intelligentdoorplateback.common.BaseConstant;
import com.scu.intelligentdoorplateback.common.JsonResponse;
import com.scu.intelligentdoorplateback.model.domain.Qrcode;
import com.scu.intelligentdoorplateback.model.domain.Record;
import com.scu.intelligentdoorplateback.model.domain.User;
import com.scu.intelligentdoorplateback.model.dto.PageDTO;
import com.scu.intelligentdoorplateback.model.vo.UserVO;
import com.scu.intelligentdoorplateback.service.QrcodeService;
import com.scu.intelligentdoorplateback.service.RecordService;
import com.scu.intelligentdoorplateback.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * 前端控制器
 *
 * @author chen
 * @version v1.0
 * @since 2023-02-28
 */
@Controller
@RequestMapping("/api/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private QrcodeService qrcodeService;
    @Autowired
    private RecordService recordService;

    /**
     * 登录
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse login(@RequestBody User user) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("phone_number", user.getPhoneNumber());
        wrapper.eq("password", user.getPassword());
        User ret = userService.getOne(wrapper);
        if (ret == null) {
            return JsonResponse.message(true, "check phone and password");
        } else {
            return JsonResponse.success(ret);
        }
    }

    /**
     * 修改密码
     */
    @PostMapping(value = "changePassword")
    @ResponseBody
    public JsonResponse changePassword(@RequestBody User user) {
        User origin = userService.getById(user.getId());
        origin.setPassword(user.getPassword());
        boolean b = userService.updateById(origin);
        return JsonResponse.message(b,"修改密码");
    }

    /**
     * 描述：户主录入信息
     */
    @RequestMapping(value = "addInfo", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse addInfo(@RequestBody UserVO userVO) {
        Long doorId = userVO.getDoorId();
        Qrcode qrcode = qrcodeService.getById(doorId);
        //获取得到的身份证号码
        String idNumber = userVO.getIdNumber();
        //根据id number查询得到用户
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id_number", idNumber);
        User user1 = userService.getOne(userQueryWrapper);
        if (user1 != null) {
            if (user1.getIsResidence()) {//查询得到用户信息
                //更新qrcode表，将qrcode表中的is_bound设置为1，即已经完成绑定
                qrcode.setAddress(userVO.getAddress());
                qrcode.setIsBound(true);
                qrcode.setUserId(user1.getId());
                boolean b = qrcodeService.updateById(qrcode);

                //更新户主的user表的用户信息
                user1.setName(userVO.getName());
                user1.setGender(userVO.getGender());
                user1.setPhoneNumber(userVO.getPhoneNumber());
                user1.setEmail(userVO.getEmail());
                user1.setAddress(userVO.getAddress());
                user1.setRoleId(BaseConstant.ROLE_MAP.get("户主"));
                boolean b1 = userService.updateById(user1);

                return JsonResponse.success((b & b1), "该用户已存在，更新信息");
            } else {
                return JsonResponse.success(false, "你还未拥有居住证，请去申请");
            }
        } else {
            User user2 = new User();
            user2.setName(userVO.getName());
            user2.setGender(userVO.getGender());
            user2.setIdNumber(idNumber);
            user2.setPhoneNumber(userVO.getPhoneNumber());
            user2.setEmail(userVO.getEmail());
            user2.setAddress(userVO.getAddress());
            user2.setIsResidence(true);
            user2.setRoleId(BaseConstant.ROLE_MAP.get("户主"));
            boolean b = userService.saveOrUpdate(user2);

            qrcode.setAddress(userVO.getAddress());
            qrcode.setIsBound(true);
            qrcode.setUserId(user2.getId());
            boolean b1 = qrcodeService.updateById(qrcode);

            return JsonResponse.success((b & b1), "该用户不存在，增加成功");
        }
    }

    /**
     * 描述：房东登记承租人信息
     */
    @RequestMapping(value = "registerTenantry", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse registerTenantry(@RequestBody UserVO userVO) {
        //获取二维码id和身份证号码
        Long doorId = userVO.getDoorId();
        System.out.println(doorId);
        String idNumber = userVO.getIdNumber();
        System.out.println(qrcodeService.getById(doorId));
        //设置该房子is_rent字段为true
        qrcodeService.updateById((qrcodeService.getById(doorId)).setIsRented(true));
        //查询身份证号码得到User
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id_number", idNumber);
        User user1 = userService.getOne(userQueryWrapper);
        Record record = recordService.getByQrId(doorId);
        if (user1 != null) {
            if(user1.getId()==record.getTenantId()) {
                user1.setName(userVO.getName());
                user1.setGender(userVO.getGender());
                user1.setPhoneNumber(userVO.getPhoneNumber());
                user1.setEmail(userVO.getEmail());
                user1.setAddress(userVO.getAddress());
                user1.setIsResidence(true);
                user1.setRoleId(BaseConstant.ROLE_MAP.get("承租人"));

                boolean b = userService.updateById(user1);
                return JsonResponse.message(b, "承租人信息更新成功");
            }else {
                return JsonResponse.message(false,"承租人信息有误");
            }
        } else {
//            User user2 = new User();
//            user2.setName(userVO.getName());
//            user2.setGender(userVO.getGender());
//            user2.setIdNumber(userVO.getIdNumber());
//            user2.setPhoneNumber(userVO.getPhoneNumber());
//            user2.setEmail(userVO.getEmail());
//            user2.setAddress(userVO.getAddress());
//            user2.setIsResidence(true);
//            user2.setRoleId(BaseConstant.ROLE_MAP.get("承租人"));
//            boolean save = userService.save(user2);
            return JsonResponse.message(false, "此房间无承租人");
        }
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse listPage(PageDTO pageDTO) {
        Page<User> page = new Page<>(pageDTO.getPageNo(), pageDTO.getPageSize());
        userService.page(page);
        return JsonResponse.success(page);
    }

    /**
     * 描述：根据Id 查询
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getById(@PathVariable("id") Long id) throws Exception {
        User user = userService.getById(id);
        return JsonResponse.success(user);
    }

    /**
     * 描述：根据Id删除
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public JsonResponse deleteById(@PathVariable("id") Long id) throws Exception {
        userService.removeById(id);
        return JsonResponse.success(null);
    }


    /**
     * 描述：根据Id 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse updateUser(@RequestBody User user) throws Exception {
        userService.updateById(user);
        return JsonResponse.success(null);
    }


    /**
     * 描述:创建User
     */
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse create(@RequestBody User user) throws Exception {
        userService.save(user);
        return JsonResponse.success(null);
    }

    /**
     * 描述:身份验证
     */
    @RequestMapping(value = "/identification", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse identification(@RequestBody User user) {
        String idNumber = user.getIdNumber();
        String email = user.getEmail();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("email", email).eq("id_number", idNumber);
        User user1 = userService.getOne(wrapper);
        if (user1 != null) {
            return JsonResponse.success(user1);
        }
        return JsonResponse.failure("身份证或邮箱错误");
    }
}

