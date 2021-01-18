package com.dangqun.aspect;

import com.auth0.jwt.JWT;
import com.dangqun.constant.Constants;
import com.dangqun.vo.restful.Result;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
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
@Order(2)
public class ManagerCheckAspect {

    /**
     * 切点：扫描用@CheckIsManager注解标注的方法
     */
    @Pointcut("@annotation(com.dangqun.annotation.CheckIsManager)")
    public void checkManager(){}

    @Around("checkManager()")
    public Result checkManager(ProceedingJoinPoint point) throws Throwable {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String token = request.getHeader("token");
        int userAuthLevel;
        try {
            userAuthLevel = Integer.valueOf(JWT.decode(token).getAudience().get(1));
        }catch (Exception e){
            return Result.failure("权限检测错误");
        }
        if(Constants.AUTH_LEVEL_ADMIN == userAuthLevel){
            return (Result)point.proceed();
        }else {
            return Result.failure("此操作无权限");
        }
    }
}
