package com.dangqun.exception;

import com.dangqun.vo.restful.Result;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
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
}
