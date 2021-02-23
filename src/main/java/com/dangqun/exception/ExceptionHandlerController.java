package com.dangqun.exception;

import com.dangqun.vo.restful.Result;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wcy
 */
@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Result exception(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        List<String> failureList = new ArrayList<>();
        for (ObjectError error : allErrors){
            FieldError fieldError = (FieldError)error;
            failureList.add(fieldError.getField() + ":" + fieldError.getDefaultMessage());
        }
        return Result.failure(failureList.toString());
    }

    @ExceptionHandler(value = MultipartException.class)
    @ResponseBody
    public Result eofException(MultipartException e){
        System.out.println("上传中断");
        return Result.failure();
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result exception(Exception e){
        e.printStackTrace();
        return Result.failure("500","系统异常");
    }
}
