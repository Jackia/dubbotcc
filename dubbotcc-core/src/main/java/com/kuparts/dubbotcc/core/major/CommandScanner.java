package com.kuparts.dubbotcc.core.major;

import com.kuparts.dubbotcc.core.dispatch.Command;
import com.kuparts.dubbotcc.core.dispatch.propety.CommandType;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.TypeFilter;

import java.util.Set;

/**
 * 事务调度命令扫描
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class CommandScanner extends ClassPathBeanDefinitionScanner {

    public CommandScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        return super.doScan(basePackages);
    }

    @Override
    public void addIncludeFilter(TypeFilter includeFilter) {
        super.addIncludeFilter(includeFilter);
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return super.isCandidateComponent(beanDefinition) && beanDefinition.getMetadata()
                .hasAnnotation(CommandType.CommandTask.class.getName())
                && beanDefinition.getMetadata().getInterfaceNames()[0].equals(Command.class.getName());
    }
}
