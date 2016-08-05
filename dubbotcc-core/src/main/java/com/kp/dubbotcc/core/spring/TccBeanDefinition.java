package com.kp.dubbotcc.core.spring;

import org.springframework.beans.factory.support.AbstractBeanDefinition;

/**
 * project：dubbotcc-parent /www.kuparts.com
 * Created By chenbin on 2016/8/4 15:05
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class TccBeanDefinition extends AbstractBeanDefinition {

    private String parentName;

    @Override
    public AbstractBeanDefinition cloneBeanDefinition() {
        return new TccBeanDefinition();
    }

    @Override
    public String getParentName() {
        return this.parentName;
    }

    @Override
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}
