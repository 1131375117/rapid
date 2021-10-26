package cn.huacloud.taxpreference.schedule.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 初始角色信息
 * 只创建管理员信息，其他信息使用手工进行录入，随后导出初始化SQL，使用SQL进行数据初始化
 * @author wangkh
 */
@Slf4j
@Component
public class RoleInitializer implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {

    }
}
