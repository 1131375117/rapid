package cn.huacloud.taxpreference.services.common.limit;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author fuhua
 */
@Slf4j
@Component
public class DefaultLimitManager implements LimitManager {

    @Override
    public double acquireToken(ConfigLimitDTO configLimitDto) {
        return 0;
    }

    @Override
    public boolean acquire(ConfigLimitDTO configLimitDto) {
        return false;
    }
}
