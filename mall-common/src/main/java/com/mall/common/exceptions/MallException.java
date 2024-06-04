package com.mall.common.exceptions;

/**
 * 项目异常类
 *
 * @author : [lm]
 * @version : [v1.0]
 * @createTime : [2023/10/25 22:07]
 */
public class MallException extends RuntimeException {
    private String code;
    private String errMessage;

    public MallException() {
        super();
    }

    public MallException(String errMessage) {
        super(errMessage);
        this.errMessage = errMessage;
    }

    public MallException(String code, String errMessage) {
        super(errMessage);
        this.code = code;
        this.errMessage = errMessage;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public static void cast(CommonError commonError) {
        throw new MallException(commonError.getCode(), commonError.getErrMessage());
    }

    public static void cast(String errMessage) {
        throw new MallException(errMessage);
    }

    public static void cast(String code, String errMessage) {
        throw new MallException(code, errMessage);
    }
}
