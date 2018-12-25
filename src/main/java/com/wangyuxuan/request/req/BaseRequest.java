package com.wangyuxuan.request.req;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Auther: wangyuxuan
 * @Date: 2018/12/25 16:57
 * @Description:
 */

@Getter
@Setter
public abstract class BaseRequest implements Serializable {

    private static final long serialVersionUID = -2631611234705594587L;

    /**
     * 请求号
     */
    private String reqNo;

    /**
     * 当前请求的时间戳
     */
    private int timeStamp;

    public BaseRequest() {
        this.setTimeStamp((int) (System.currentTimeMillis() / 1000));
    }
}
