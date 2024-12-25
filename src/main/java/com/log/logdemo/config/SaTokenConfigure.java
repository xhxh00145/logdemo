package com.log.logdemo.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.jwt.SaJwtTemplate;
import cn.dev33.satoken.jwt.SaJwtUtil;
import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.jwt.StpLogicJwtForStateless;
import cn.dev33.satoken.jwt.exception.SaJwtException;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaFoxUtil;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTException;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {
    // Sa-Token 整合 jwt (Simple 简单模式)
    @Bean
    public StpLogic getStpLogicJwt() {
        System.out.println(String.format("getStpLogicJwt:%s",Integer.valueOf(1).toString()));
        return new StpLogicJwtForStateless();
    }

    /**
     * 自定义 SaJwtUtil 生成 token 的算法
     */
    @PostConstruct
    public void setSaJwtTemplate() {
        SaJwtUtil.setSaJwtTemplate(new SaJwtTemplate() {
            @Override
            public String generateToken(JWT jwt, String keyt) {
                System.out.println(String.format("setSaJwtTemplate:%s,JWT=%s,keyt=%s",Integer.valueOf(2).toString(),jwt,keyt));
                System.out.println("------ 自定义了 token 生成算法");
                //新的加密算法
                return jwt.setSigner(JWTSignerUtil.hs384(keyt.getBytes())).sign();
                //原来的加密算法
//                return super.generateToken(jwt, keyt);
            }

            public JWT parseToken(String token, String loginType, String keyt, boolean isCheckTimeout) {
                if (SaFoxUtil.isEmpty(keyt)) {
                    throw new SaJwtException("请配置 jwt 秘钥");
                } else if (token == null) {
                    throw new SaJwtException("jwt 字符串不可为空");
                } else {
                    JWT jwt;
                    try {
                        jwt = JWT.of(token);
                    } catch (JSONException | JWTException var9) {
                        throw (new SaJwtException("jwt 解析失败：" + token, var9)).setCode(30201);
                    }

                    JSONObject payloads = jwt.getPayloads();
                    //原来的加密算法
//                    boolean verify = jwt.setSigner(this.createSigner(keyt)).verify();
                    //自定义的加密算法
                    boolean verify = jwt.setSigner(JWTSignerUtil.hs384(keyt.getBytes())).verify();
                    if (!verify) {
                        throw (new SaJwtException("jwt 签名无效：" + token)).setCode(30202);
                    } else if (!Objects.equals(loginType, payloads.getStr("loginType"))) {
                        throw (new SaJwtException("jwt loginType 无效：" + token)).setCode(30203);
                    } else {
                        if (isCheckTimeout) {
                            Long effTime = payloads.getLong("eff", 0L);
                            if (effTime != -1L && (effTime == null || effTime < System.currentTimeMillis())) {
                                throw (new SaJwtException("jwt 已过期：" + token)).setCode(30204);
                            }
                        }

                        return jwt;
                    }
                }
            }

        });
    }



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println(String.format("addInterceptors:%s",Integer.valueOf(3).toString()));

        // 注册 Sa-Token 拦截器，定义详细认证规则
        registry.addInterceptor(new SaInterceptor(handler -> {
            // 指定一条 match 规则
            SaRouter
                    .match("/**")    // 拦截的 path 列表，可以写多个 */
                    .notMatch("/jwt/doLogin")        // 排除掉的 path 列表，可以写多个
                    .check(r -> StpUtil.checkLogin());        // 要执行的校验动作，可以写完整的 lambda 表达式

            // 根据路由划分模块，不同模块不同鉴权
//            SaRouter.match("/user/**", r -> StpUtil.checkPermission("user"));
//            SaRouter.match("/admin/**", r -> StpUtil.checkPermission("admin"));
//            SaRouter.match("/goods/**", r -> StpUtil.checkPermission("goods"));
//            SaRouter.match("/orders/**", r -> StpUtil.checkPermission("orders"));
//            SaRouter.match("/notice/**", r -> StpUtil.checkPermission("notice"));
//            SaRouter.match("/comment/**", r -> StpUtil.checkPermission("comment"));
        })).addPathPatterns("/**");
    }
}

