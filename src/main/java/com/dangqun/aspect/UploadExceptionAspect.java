package com.dangqun.aspect;

import com.dangqun.vo.restful.Result;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author wcy
 */
@Aspect
@Component
@Order(3)
public class UploadExceptionAspect {
    @Pointcut("@annotation(com.dangqun.annotation.FileExceptionRollBack)")
    public void RollBack(){}

    @Around(value = "RollBack()")
    public Result doAfterAdvice(ProceedingJoinPoint point) throws Throwable {
        System.out.println("环绕监听开始");
        Result result = (Result) point.proceed();
        System.out.println("环绕监听jieshu");

        if("500".equals(result.getRespCode()) && "上传中断".equals(result.getRespDesc())){
            System.out.println("环绕监听启动");
        }
        return result;
    }

}
