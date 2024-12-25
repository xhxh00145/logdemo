package com.log.logdemo.controller;

import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * JWT登录鉴权
 */
@RestController
@RequestMapping("/jwt/")
public class LoginController {

    // 登录接口
    @RequestMapping("doLogin")
    public SaResult doLogin() {
        // 第1步，先登录上
        StpUtil.login(10001, SaLoginConfig.setExtra("name", "zhangsan"));
        // 第2步，获取 Token  相关参数
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        // 第3步，返回给前端
        return SaResult.data(tokenInfo);
    }

    // 查询登录状态
    @RequestMapping("isLogin")
    public SaResult isLogin() {
        System.out.println(StpUtil.getLoginId());
        System.out.println(StpUtil.getExtra("name"));
        return SaResult.ok("是否登录：" + StpUtil.isLogin());
    }

    // 测试注销
    @RequestMapping("logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok();
    }

}
