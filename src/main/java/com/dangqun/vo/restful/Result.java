package com.dangqun.vo.restful;

/**
 * @author wcy
 */
public class Result<T> {

    public static final String FAILURE_RESPCODE = "-9999";
    public static final String SUCCESS_RESPCODE = "0";

    private String respCode;
    private String respDesc;
    private T object;

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespDesc() {
        return respDesc;
    }

    public void setRespDesc(String respDesc) {
        this.respDesc = respDesc;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public static Result success() {
        Result result = new Result();
        result.setRespCode(SUCCESS_RESPCODE);
        result.setRespDesc("成功");
        return result;
    }

    public static Result success(Object data) {
        Result result = new Result();
        result.setRespCode(SUCCESS_RESPCODE);
        result.setRespDesc("成功");
        result.setObject(data);
        return result;
    }

    public static Result failure() {
        Result result = new Result();
        result.setRespCode(FAILURE_RESPCODE);
        result.setRespDesc("失败");
        return result;
    }

    public static Result failure(String respDesc){
        Result result = new Result();
        result.setRespCode(FAILURE_RESPCODE);
        result.setRespDesc(respDesc);
        return result;
    }

    public static Result failure(String respCode,String respDesc){
        Result result = new Result();
        result.setRespCode(respCode);
        result.setRespDesc(respDesc);
        return result;
    }


    @Override
    public String toString() {
        return "Result{" +
                "respCode:'" + respCode + '\'' +
                ", respDesc:'" + respDesc + '\'' +
                ", object:" + object +
                '}';
    }
}
