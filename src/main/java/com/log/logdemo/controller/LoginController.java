package com.log.logdemo.controller;

import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.log.logdemo.po.Test;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * JWT登录鉴权
 */
@RestController
@RequestMapping("/jwt/")
public class LoginController {

    /**
     * 访问地址 http://localhost:8083/jwt/doLogin
     *
     * @return
     */
    @RequestMapping("doLogin")
    public SaResult doLogin() {
        Test test = new Test(10001, "胥浩", 32);
        // 第1步，先登录上
        StpUtil.login(10001,
                SaLoginConfig
                        .setExtra("name", "zhangsan")
                        .setExtra("test", test)
        );
        // 第2步，获取 Token  相关参数
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        // 第3步，返回给前端
        return SaResult.data(tokenInfo);
    }

    /**
     * http://localhost:8083/jwt/doLogin
     *
     * @return
     */
    @RequestMapping("isLogin")
    public SaResult isLogin() {
        System.out.println(StpUtil.getLoginId());
        System.out.println(StpUtil.getExtra("name"));

        JSONObject test =(JSONObject)StpUtil.getExtra("test");
        test.putOpt("aaaaa", 111111111);
        System.out.println(11111111);
        System.out.println(test.getClass());
        System.out.println(test);
        Test test1 = null;
        // 使用 Jackson 将 JSONObject 转换为 Test 对象
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setPropertyNamingStrategy(PropertyNamingStrategies.UPPER_CAMEL_CASE);
        try {
            test1 = objectMapper.readValue(test.toString(), Test.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(test1);

        // 创建一个 Hutool 的 JSONObject
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("Id", 10001);
//        jsonObject.put("Name", "admin");
//        jsonObject.put("Age", 32);

//        // 使用 Jackson 将 JSONObject 转换为 Test 对象
//        ObjectMapper objectMapper1 = new ObjectMapper();
//        objectMapper1.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        objectMapper1.setPropertyNamingStrategy(PropertyNamingStrategies.UPPER_CAMEL_CASE);
//
//        try {
//            Test test2 = objectMapper1.readValue(jsonObject.toString(), Test.class);
//            System.out.println(test2);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return SaResult.ok("是否登录：" + StpUtil.isLogin());
    }

    // 测试注销 jwt无状态无效 token此时在客户端
    @RequestMapping("logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok();
    }

}
