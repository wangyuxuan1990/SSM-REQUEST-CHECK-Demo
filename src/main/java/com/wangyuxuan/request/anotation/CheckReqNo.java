package com.wangyuxuan.request.anotation;

import java.lang.annotation.*;

/**
 * @Auther: wangyuxuan
 * @Date: 2018/12/25 16:50
 * @Description:
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckReqNo {

    String desc() default "";
}
