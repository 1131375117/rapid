package cn.huacloud.taxpreference.sync.es.trigger;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;

import java.util.Set;

/**
 * @author wangkh
 */
public class EventTriggerScannerConfigurer implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        ClassPathEventTriggerScanner scanner = new ClassPathEventTriggerScanner(registry);
        scanner.setResourceLoader(applicationContext);
        scanner.resetFilters(false);
        scanner.addIncludeFilter((r, f) -> true);
        int scan = scanner.scan("cn.huacloud.taxpreference.sync.es.trigger.impl");
        System.out.println(scan);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    static class ClassPathEventTriggerScanner extends ClassPathBeanDefinitionScanner {

        public ClassPathEventTriggerScanner(BeanDefinitionRegistry registry) {
            super(registry);
        }

        @Override
        protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
            return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
        }

        @Override
        protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
            Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
            processBeanDefinitions(beanDefinitionHolders);
            return beanDefinitionHolders;
        }

        private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
            for (BeanDefinitionHolder holder : beanDefinitions) {

                ScannedGenericBeanDefinition beanDefinition = (ScannedGenericBeanDefinition) holder.getBeanDefinition();
                beanDefinition.setBeanClass(EventTriggerFactoryBean.class);
            }
        }
    }

    public static class EventTriggerFactoryBean<T> implements FactoryBean<T> {

        private Class<T> eventTriggerInterface;

        public EventTriggerFactoryBean(Class<T> eventTriggerInterface) {
            this.eventTriggerInterface = eventTriggerInterface;
        }

        @Override
        public T getObject() throws Exception {
            return null;
        }

        @Override
        public Class<?> getObjectType() {
            return null;
        }
    }
}
