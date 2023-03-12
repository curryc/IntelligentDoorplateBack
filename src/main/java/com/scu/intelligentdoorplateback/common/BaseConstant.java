package com.scu.intelligentdoorplateback.common;

import java.util.HashMap;
import java.util.Map;

public interface BaseConstant {
    String[] ROLES = {"超级管理员","民警","巡警","居民"};

    Map<String,Integer> ROLE_MAP = new HashMap<String,Integer>(){{
        put("超级管理员",1);
        put("民警",2);
        put("巡警",3);
        put("户主",4);
        put("承租人",5);
        put("普通人",6);
    }};
    Map<String,Integer> TASK_TYPE = new HashMap<String,Integer>(){{
        put("定时任务",1);
        put("临时任务",2);
    }};

    Map<String,Integer> DOORPLATE_TYPE = new HashMap<String,Integer>(){{
        put("单个房间",1);
        put("多个房间",2);
    }};

    Map<String,Integer> APPLY_TYPE = new HashMap<String,Integer>(){{
        put("居住证",1);
        put("租房",2);
    }};

    Map<String,Integer> GENDER = new HashMap<String,Integer>(){{
        put("男",1);
        put("女",0);
    }};

    Map<String,Integer> VERIFY_STATUS = new HashMap<String,Integer>(){{
        put("已提交",1);
        put("审核成功",2);
        put("审核失败",3);
    }};

    Map<String,Integer> INFO_TYPE = new HashMap<String,Integer>(){{
        put("房屋",1);
        put("小区",2);
    }};

}
