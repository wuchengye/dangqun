package com.dangqun.aspect;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.dangqun.entity.LogEntity;
import com.dangqun.service.LogService;
import com.dangqun.utils.DateTimeUtils;
import com.dangqun.utils.IpUtil;
import com.dangqun.vo.restful.Result;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wcy
 */
@Aspect
@Component
@Order(3)
public class LogAspect {

    @Autowired
    private LogService logService;

    @Pointcut("execution(* com.dangqun.controller..*.*(..))")
    public void log(){}

    @Around("log()")
    public Result doLog(ProceedingJoinPoint point) throws Throwable {
        String methodName = point.getSignature().getName();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String token = request.getHeader("token");
        LogEntity logEntity = new LogEntity();
        logEntity.setLogMethod(methodName);
        logEntity.setLogIp(IpUtil.getIpAddr(request));
        if (token != null){
            logEntity.setLogUser(JWT.decode(token).getAudience().get(0));
        }
        logEntity.setLogParams(JSONObject.toJSONString(point.getArgs()));
        Result logResult = (Result) point.proceed();
        logEntity.setLogReturn(logResult.toString());
        logEntity.setLogTime(DateTimeUtils.getTime());
        logService.insertOne(logEntity);
        return logResult;
    }
}
