package com.seckill.dto;

/**
 * 封装所有的ajax请求返回类型，方便返回json
 *
 * @author hyh47
 *
 */
public class SeckillResult<T> {

    //执行的结果
    private boolean success;
    //泛型数据
    private T data;
    //字符串类型的具体error
    private String error;

    public SeckillResult(boolean success, T data) {
        super();
        this.success = success;
        this.data = data;
    }

    public SeckillResult(boolean success, String error) {
        super();
        this.success = success;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "SeckillResult [success=" + success + ", data=" + data + ", error=" + error + "]";
    }

}
