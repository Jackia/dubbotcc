package com.kuparts.dubbotcc.core.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application.xml")
public class SpringLoadTest{
    @Test
    public void test(){
        System.out.println("加载");
    }
}
