package com.example.springboot.common;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 权限拦截器
 * 拦截请求并验证用户身份和权限
 */
@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                            HttpServletResponse response,
                            Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        RequireRole requireRole = handlerMethod.getMethodAnnotation(RequireRole.class);

        if (requireRole == null) {
            return true;
        }

        // 注意：这里的 "Identity" 必须和登录时存储的Key一致
        String identity = (String) request.getSession().getAttribute("Identity");

        if (identity == null) {
            log.warn("未授权访问: {}", request.getRequestURI());
            return unauthorized(response, "请先登录");
        }

        String[] allowedRoles = requireRole.value();
        Set<String> roleSet = new HashSet<>(Arrays.asList(allowedRoles));

        if (!roleSet.contains(identity)) {
            log.warn("权限不足: user={}, uri={}, requiredRoles={}",
                    identity, request.getRequestURI(), Arrays.toString(allowedRoles));
            return forbidden(response, "权限不足");
        }

        return true;
    }

    /**
     * 返回401未授权响应
     *
     * 注意：必须手动构建响应，因为拦截器在Controller之前执行，全局异常处理器捕获不到
     */
    private boolean unauthorized(HttpServletResponse response, String message) throws Exception {
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        Result result = Result.error("401", message);
        writer.write(JSON.toJSONString(result));
        writer.flush();
        writer.close();
        return false;
    }

    /**
     * 返回403禁止访问响应
     */
    private boolean forbidden(HttpServletResponse response, String message) throws Exception {
        response.setStatus(403);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        Result result = Result.error("403", message);
        writer.write(JSON.toJSONString(result));
        writer.flush();
        writer.close();
        return false;
    }
}
