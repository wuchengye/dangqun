package com.dangqun.aspect;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.dangqun.service.common.RedisService;
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
import java.util.Map;

/**
 * @author wcy
 */
@Aspect
@Component
@Order(1)
public class JwtAsepect {

    public static final String LOGIN_METHOD_NAME = "login";

    @Autowired
    private RedisService redisService;

    /**
     * 定义切点:扫描controller包下所有类中方法，但排除加了PassToken注解的方法.
     */
    @Pointcut("execution(* com.dangqun.controller.*.*(..)) && !@annotation(com.dangqun.annotation.PassToken)")
    public void jwt(){}

    @Around("jwt()")
    public Result jwtCheck(ProceedingJoinPoint point) throws Throwable {
        //判断是否是登录接口
        String methodName = point.getSignature().getName();
        if(LOGIN_METHOD_NAME.equals(methodName)){
            Result loginResult = (Result) point.proceed();
            if (loginResult.getRespCode().equals(Result.FAILURE_RESPCODE)){
                return loginResult;
            }else {
                Map map = (Map) loginResult.getObject();
                String time = String.valueOf(System.currentTimeMillis());
                String token= JWT.create().withAudience((String)map.get("userName"),String.valueOf((int)map.get("userAuthLevel")),time)
                        .sign(Algorithm.HMAC256((String) map.get("userPwd") + time));
                //存入缓存
                redisService.setLoginValid((String)map.get("userName"),(String) map.get("userPwd") + time);
                map.put("token",token);
                map.remove("userPwd");
                return Result.success(map);
            }
        }else {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = requestAttributes.getRequest();
            String token = request.getHeader("token");
            if(token == null){
                return Result.failure("403","无token");
            }
            String userName;
            try {
                userName = JWT.decode(token).getAudience().get(0);
            } catch (JWTDecodeException j) {
                return Result.failure("403","无效token");
            }
            String userPwdAndTime = redisService.getLoginValid(userName);
            if(userPwdAndTime == null){
                return Result.failure("403","token过期");
            }
            // 验证 token
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(userPwdAndTime)).build();
            try {
                jwtVerifier.verify(token);
            } catch (JWTVerificationException e) {
                return Result.failure("403","token错误");
            }
            redisService.expireLoginValid(userName);
            return (Result) point.proceed();
        }
    }


}
