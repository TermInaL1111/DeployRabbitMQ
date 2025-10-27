package com.shms.deployrabbitmq.pojo;

import lombok.Data;

//请求返回结果封装
import lombok.Data;

//@Data
//public class Result<T> {
//    private int code;
//    private String msg;
//    private T data;  // 泛型字段，接收任意类型
//
//    // 成功响应（带数据）
//    public static <T> Result<T> success(T data) {
//        Result<T> result = new Result<>();
//        result.setCode(1);
//        result.setData(data);
//        return result;
//    }
//
//    // 错误响应（带消息）
//    public static <T> Result<T> error(String msg) {
//        Result<T> result = new Result<>();
//        result.setCode(0);
//        result.setMsg(msg);
//        return result;
//    }
//}
@Data
public class Result {

    private Integer code;//编码 1,0 成功失败
    private String msg;//错误信息
    private Object data;//数据

    public static Result success(){
        Result result = new Result();
        result.code = 1;
        result.msg = "success";
        return result;
    }
    public static Result success(Object object){
        Result result = new Result();
        result.code = 1;
        result.msg = "success";
        result.data = object;
        return result;
    }
    public static Result error(String msg){
        Result result = new Result();
        result.code = 0;
        result.msg = msg;
        return result;
    }

}