package com.scu.intelligentdoorplateback.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scu.intelligentdoorplateback.common.BaseConstant;
import com.scu.intelligentdoorplateback.common.JsonResponse;
import com.scu.intelligentdoorplateback.common.utils.MailUtils;
import com.scu.intelligentdoorplateback.common.utils.TimeUtils;
import com.scu.intelligentdoorplateback.model.domain.Apply;
import com.scu.intelligentdoorplateback.model.domain.Qrcode;
import com.scu.intelligentdoorplateback.model.domain.Record;
import com.scu.intelligentdoorplateback.model.domain.User;
import com.scu.intelligentdoorplateback.model.dto.PageDTO;
import com.scu.intelligentdoorplateback.model.vo.ApplyVO;
import com.scu.intelligentdoorplateback.model.vo.UserVO;
import com.scu.intelligentdoorplateback.service.ApplyService;
import com.scu.intelligentdoorplateback.service.QrcodeService;
import com.scu.intelligentdoorplateback.service.RecordService;
import com.scu.intelligentdoorplateback.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 前端控制器
 *
 * @author chen
 * @version v1.0
 * @since 2023-03-01
 */
@Controller
@RequestMapping("/api/apply")
public class ApplyController {

    private final Logger logger = LoggerFactory.getLogger(ApplyController.class);

    @Autowired
    private ApplyService applyService;
    @Autowired
    private UserService userService;
    @Autowired
    private QrcodeService qrcodeService;
    @Autowired
    private RecordService recordService;

    /**
     * 户主收到的租房请求
     *
     * @param processorId
     * @return
     */
    @RequestMapping(value = "mine", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getMine(@RequestParam("id") Long processorId) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("processor_id", processorId);
        wrapper.eq("verify_status", BaseConstant.VERIFY_STATUS.get("已提交"));
        Page<Apply> ret = applyService.page(new Page<Apply>(0, 1000), wrapper);
        return JsonResponse.success(ret);
    }


    @RequestMapping(value = "rent", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse rent(@RequestBody ApplyVO applyVO) {
        //创建一个申请用例
        Apply apply = new Apply();

//        System.out.println(applyVO);

        //得到唯一的标识符，身份证号
        String idNumber = applyVO.getIdNumber();

        //根据id number查询得到用户
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id_number", idNumber);
        User user1 = userService.getOne(userQueryWrapper);

        //设置apply的基本信息

        Long hostId = qrcodeService.getById(applyVO.getDoorId()).getUserId();
        apply.setProcessorId(hostId);
        apply.setName(applyVO.getName());
        apply.setGender(applyVO.getGender());
        apply.setIdNumber(applyVO.getIdNumber());
        apply.setPhoneNumber(applyVO.getPhoneNumber());
        apply.setEmail(applyVO.getEmail());
        apply.setAddress(applyVO.getAddress());
        apply.setVerifyStatus(BaseConstant.VERIFY_STATUS.get("已提交"));
        apply.setApplyTime(TimeUtils.toLocalDateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        apply.setStartTime(applyVO.getStartTime());
        apply.setEndTime(applyVO.getEndTime());
        apply.setType(BaseConstant.APPLY_TYPE.get("租房"));
        //判断用户是否存在
        if (user1 != null) {
            if (!user1.getId().equals(hostId)) {
                if (user1.getIsResidence()) {

                    //更新用户信息
                    user1.setName(applyVO.getName());
                    user1.setGender(applyVO.getGender());
                    user1.setPhoneNumber(applyVO.getPhoneNumber());
                    user1.setEmail(applyVO.getEmail());
                    user1.setAddress(applyVO.getAddress());

                    //更新数据库中的用户信息
                    boolean b = userService.updateById(user1);

                    //向apply表中添加记录
                    apply.setUserId(user1.getId());
                    boolean save = applyService.save(apply);

                    return JsonResponse.message(b && save, "用户信息已更新，申请成功！");

                } else {
                    return JsonResponse.message(false, "你还没有居住证");
                }
            } else {
                return JsonResponse.message(false, "不要租自己的房");
            }
        } else {
            //向用户表中添加用户记录
            User user2 = new User();
            user2.setName(applyVO.getName());
            user2.setGender(applyVO.getGender());
            user2.setIdNumber(applyVO.getIdNumber());
            user2.setPhoneNumber(applyVO.getPhoneNumber());
            user2.setEmail(applyVO.getEmail());
            user2.setAddress(applyVO.getAddress());
            user2.setIsResidence(true);
            user2.setRoleId(BaseConstant.ROLE_MAP.get("普通人"));
            boolean b = userService.save(user2);
            //添加apply记录
            apply.setUserId(user2.getId());
            boolean save = applyService.save(apply);
            return JsonResponse.message(b && save, "用户信息已添加，申请成功!");
        }
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse listPage(PageDTO pageDTO) {
        System.out.println(pageDTO);
        Page<Apply> page = new Page<>(pageDTO.getPageNo(), pageDTO.getPageSize());
        applyService.page(page);
        return JsonResponse.success(page);
    }


    /**
     * 描述：根据Id 查询
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getById(@PathVariable("id") Long id) throws Exception {
        Apply apply = applyService.getById(id);
        return JsonResponse.success(apply);
    }

    /**
     * 描述：根据Id删除
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public JsonResponse deleteById(@PathVariable("id") Long id) throws Exception {
        applyService.removeById(id);
        return JsonResponse.success(null);
    }


    /**
     * 描述：根据Id 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse updateApply(@RequestBody Apply apply) throws Exception {
        System.out.println(apply);
        applyService.updateById(apply);
        return JsonResponse.success(apply);
    }


    /**
     * 描述:创建Apply
     */
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse create(@RequestBody Apply apply) throws Exception {
        applyService.save(apply);
        return JsonResponse.success(null);
    }

    /**
     * 描述：流动人口提交申请表
     * 输入流动人口的姓名、性别、电话号码、身份证、电子邮箱上传到后端，后端录入数据
     * 如果user存在则只生成apply表，如果user不存在则生成apply表和新user表
     */

    @PostMapping(value = "import")
    @ResponseBody
    public JsonResponse importApply(@RequestBody Apply apply) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id_number", apply.getIdNumber());
        User user = userService.getOne(userQueryWrapper);
        if (user != null) {
            if (!user.getIsResidence()) {
                QueryWrapper<Apply> applyQueryWrapper = new QueryWrapper<>();
                applyQueryWrapper.eq("user_id", user.getId());
                applyQueryWrapper.eq("verify_status", BaseConstant.VERIFY_STATUS.get("已提交"));
                Apply one = applyService.getOne(applyQueryWrapper);
                if (one == null) {
                    apply.setProcessorId(1L)
                            .setApplyTime(LocalDateTime.now())
                            .setVerifyStatus(BaseConstant.VERIFY_STATUS.get("已提交"))
                            .setType(BaseConstant.APPLY_TYPE.get("居住证"))
                            .setUserId(user.getId());
                    applyService.save(apply);
                    // 根据填入的apply表更新user表
                    user.setName(apply.getName())
                            .setPhoneNumber(apply.getPhoneNumber())
                            .setIdNumber(apply.getIdNumber())
                            .setGender(apply.getGender())
                            .setEmail(apply.getEmail())
                            .setAddress(apply.getAddress())
                            .setIsResidence(false);
                    userService.saveOrUpdate(user); // 将修改后的user对象保存到数据库中
                    return JsonResponse.message(true, "user存在，只生成apply表，申请成功！");
                } else {
                    return JsonResponse.message(false, "你已经申请过了");
                }
            } else {
                return JsonResponse.message(false, "你已经有居住证");
            }
        } else {
            // 新生成一个user
            user = new User();
            // 在Apply表中添加信息
            apply
                    .setProcessorId(1L)
                    .setApplyTime(LocalDateTime.now())
                    .setVerifyStatus(BaseConstant.VERIFY_STATUS.get("已提交"))
                    .setType(BaseConstant.APPLY_TYPE.get("居住证"));

            // apply提交后自动添加user信息
            user.setName(apply.getName())
                    .setPhoneNumber(apply.getPhoneNumber())
                    .setIdNumber(apply.getIdNumber())
                    .setGender(apply.getGender())
                    .setEmail(apply.getEmail())
                    .setAddress(apply.getAddress())
                    .setRoleId(BaseConstant.ROLE_MAP.get("普通人"))
                    .setIsResidence(false);
            userService.save(user);
            // 将新生成的用户id赋值给apply表的user_id
            apply.setUserId(user.getId());
            applyService.save(apply);
            return JsonResponse.message(true, "user不存在，生成apply表和新user表，申请成功1！");
        }
    }

    /**
     * 描述：后台审核居住证资料
     * 后台管理员审核流动人口办理居住证提交的信息，当信息审核通过后，向系统提交取证的时间和地点。
     * (根据传入的processorId，输入取证时间和取证地点，存入apply表并返回apply表的信息）
     */
    @RequestMapping(value = "queryApply", method = RequestMethod.GET)
    @ResponseBody
    @Deprecated
    public JsonResponse queryApply(Long processorId) {
        System.out.println(processorId);
        List<Apply> applies = applyService.getUserApply(processorId);
        return JsonResponse.success(applies);
    }

    /**
     * 超管审核
     *
     * @param applyVO
     * @return
     */
    @PostMapping(value = "check")
    @ResponseBody
    public JsonResponse updateApply(@RequestBody ApplyVO applyVO) {
        Apply apply = applyService.findById(applyVO.getId());
        if (apply == null) {
            return JsonResponse.message(false, "申请表更新失败");
        } else {
            if (apply.getType() == BaseConstant.APPLY_TYPE.get("居住证")) {
                String idNumber = apply.getIdNumber();
                QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
                userQueryWrapper.eq("id_number", idNumber);
                User user = userService.getOne(userQueryWrapper);
                apply.setResTime(applyVO.getResTime())
                        .setResLocation(applyVO.getResLocation())
                        .setVerifyStatus(applyVO.getVerifyStatus());
                applyService.updateById(apply);
                if(apply.getVerifyStatus() == BaseConstant.VERIFY_STATUS.get("审核成功")) {
                    user.setIsResidence(true);
                    userService.updateById(user);
                }
//                MailUtils.sendMail(apply.getEmail(),"Intelligent Doorplate", "申请居住证反馈","你已经成功申请居住证，请在"+applyVO.getResTime()+"前在"+applyVO.getResLocation()+"取得居住证");
//            userService.updateById()
                return JsonResponse.success("申请表更新成功1");
            } else if (apply.getType() == BaseConstant.APPLY_TYPE.get("租房")) {
                // 如果是租房申请，更改apply表和qrcode表
                Long processorId = apply.getProcessorId();
                QueryWrapper<Qrcode> qrcodeQueryWrapper = new QueryWrapper<>();
                qrcodeQueryWrapper.eq("user_id", processorId);
                Qrcode qrcode = qrcodeService.getOne(qrcodeQueryWrapper);
                apply.setVerifyStatus(applyVO.getVerifyStatus());
                applyService.updateById(apply);
                // 判断审核状态是否成功，成功就赋值IsRented为true
                if(apply.getVerifyStatus() == BaseConstant.VERIFY_STATUS.get("审核成功")) {
                    qrcode.setIsRented(true);
                    qrcodeService.updateById(qrcode);
                    //审核成功后，生成record表，并将之前的record改变器rented状态
                    QueryWrapper wrapper = new QueryWrapper();
                    wrapper.eq("qr_id",qrcode.getId());
                    wrapper.eq("is_rented",1);
                    Record record1 = recordService.getOne(wrapper);

                    QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
                    userQueryWrapper.eq("id_number", apply.getIdNumber());
                    User user = userService.getOne(userQueryWrapper);
                    user.setRoleId(BaseConstant.ROLE_MAP.get("承租人"));
                    userService.saveOrUpdate(user);
                    // 先判断记录是否存在，记录不存在就直接生成如果存在就改变状态并新生成record记录
                    if (record1 != null) {
                        record1.setIsRented(false);
                        recordService.updateById(record1);
                        Record record = new Record();
                        record.setIsRented(qrcode.getIsRented())
                                .setQrId(qrcode.getId())
                                .setApplyId(apply.getId())
                                .setTenantId(apply.getUserId());
                        recordService.save(record);
                    } else {
                        Record record = new Record();
                        record.setIsRented(qrcode.getIsRented())
                                .setQrId(qrcode.getId())
                                .setApplyId(apply.getId())
                                .setTenantId(apply.getUserId());
                        recordService.save(record);
                    }
                }
                return JsonResponse.success("申请表更新成功2");
            } else {
                return JsonResponse.message(false, "申请表更新失败");
            }
        }
    }

    /**
     * 流动人口查看办证进度
     * 流动人口扫描二维码后，发送自己的id去查询apply表，返回自己目前的VerifyStatus以及ResTime和ResLocation等相关信息。
     */
    @RequestMapping(value = "queryProcess", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getProcess(Long userId) {
        List<Apply> applies = applyService.getApplies(userId);
        if (applies.isEmpty()) {
            return JsonResponse.message(true, "未查询到申请信息");
        } else {
            return JsonResponse.success(applies, "查询得到信息");
        }
    }


    /**
     * @return apply
     * 查询住房申请进度
     */
    @GetMapping(value = "/applicationprogress")
    @ResponseBody
    public JsonResponse applicationprogress(Long userId) {
        QueryWrapper<Apply> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("type", BaseConstant.APPLY_TYPE.get("租房"));
        List<Apply> apply = applyService.list(wrapper);
        if (apply != null) {
            return JsonResponse.success(apply);
        } else {
            return JsonResponse.failure("没有申请记录");
        }
    }

    /**
     * @return apply
     * 查询居住证申请进度
     */
    @GetMapping(value = "/residencepermit")
    @ResponseBody
    public JsonResponse residencepermit(String user_id) {
        QueryWrapper<Apply> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", user_id).eq("type", 1);
        List<Apply> apply = applyService.list(wrapper);
        if (apply != null) {
            return JsonResponse.success(apply);
        } else {
            return JsonResponse.failure("没有申请记录");
        }
    }


    /**
     * 户主的所有的房子的的出租历史
     * @param user_id
     * @return
     */
    @GetMapping(value = "/renthistory")
    @ResponseBody
    public JsonResponse operationhistory(Long user_id) {
        QueryWrapper<Apply> wrapper = new QueryWrapper<>();
        wrapper.eq("processor_id", user_id).eq("type", 2).eq("verify_status", BaseConstant.VERIFY_STATUS.get("审核成功"));
        List<Apply> applies = applyService.list(wrapper);
        if (applies != null) {
            return JsonResponse.success(applies);
        } else {
            return JsonResponse.failure("没有出租记录");
        }
    }
}

