package com.kp.dubbotcc.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class SpringCusmter {
    public static void main(String [] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:application.xml");
        context.getBeanFactory();
        DemoSpring demoSpring =  context.getBean(DemoSpring.class);
        demoSpring.print();
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
