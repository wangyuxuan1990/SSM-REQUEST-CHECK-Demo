package com.wangyuxuan.request.interceptor;

import com.wangyuxuan.request.req.BaseRequest;
import com.wangyuxuan.request.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: wangyuxuan
 * @Date: 2018/12/25 17:03
 * @Description:
 */

@Slf4j
@Aspect
@Component
public class ReqNoDrcAspect {

    @Value("${redis.prefixReq:reqNo}")
    private String prefixReq;

    @Value("${redis.day:1}")
    private long day;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @PostConstruct
    public void init() throws Exception {
        log.info("SSM-REQUEST-CHECK-Demo init......");
    }

    @Pointcut("@annotation(com.wangyuxuan.request.anotation.CheckReqNo))")
    public void checkRepeat() {

    }

    @Before("checkRepeat()")
    public void before(JoinPoint joinPoint) throws Exception {
        BaseRequest request = getBaseRequest(joinPoint);
        if (request != null) {
            final String reqNo = request.getReqNo();
            if (StringUtil.isEmpty(reqNo)) {
                throw new RuntimeException("reqNo不能为空");
            } else {
                try {
                    String tempReqNo = redisTemplate.opsForValue().get(prefixReq + reqNo);
                    log.debug("tempReqNo={}", tempReqNo);

                    if (StringUtil.isEmpty(tempReqNo)) {
                        redisTemplate.opsForValue().set(prefixReq + reqNo, reqNo, day, TimeUnit.DAYS);
                    } else {
                        throw new RuntimeException("请求号重复,reqNo=" + reqNo);
                    }
                } catch (RedisConnectionFailureException e) {
                    log.error("redis操作异常", e);
                    throw new RuntimeException("need redisService");
                }
            }
        }
    }

    public static BaseRequest getBaseRequest(JoinPoint joinPoint) throws Exception {
        BaseRequest returnRequest = null;
        Object[] arguments = joinPoint.getArgs();
        if (arguments != null && arguments.length > 0) {
            returnRequest = (BaseRequest) arguments[0];
        }
        return returnRequest;
    }
}
