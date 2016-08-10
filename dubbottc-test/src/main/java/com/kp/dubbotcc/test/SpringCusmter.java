package com.kp.dubbotcc.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class SpringCusmter {
    public static void main(String[] args) {
        new SpringCusmter().test();
    }

    public void test() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:application.xml");
        context.getBean(DemoSpring.class).print();
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
