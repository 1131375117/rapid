package cn.huacloud.taxpreference.sample;

        import lombok.extern.slf4j.Slf4j;
        import org.junit.Test;
        import org.springframework.beans.factory.config.BeanDefinition;
        import org.springframework.beans.factory.support.DefaultListableBeanFactory;
        import org.springframework.context.ApplicationContext;
        import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
        import org.springframework.mock.env.MockEnvironment;

        import java.util.Set;

/**
 * spring简单测试类
 *
 * @author wangkh
 */
@Slf4j
public class SpringSampleTest {

    @Test
    public void name() {
        DefaultListableBeanFactory registry = new DefaultListableBeanFactory();
        MockEnvironment environment = new MockEnvironment();
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry, false, environment);
        Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents("cn.huacloud.taxpreference.sync.es.trigger.impl");
        log.info("{}", candidateComponents.size());
    }
}
