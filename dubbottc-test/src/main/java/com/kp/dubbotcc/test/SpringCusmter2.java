package com.kp.dubbotcc.test;

import com.kuparts.dubbotcc.supervise.support.ParentSuperviseService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class SpringCusmter2 {
    public static void main(String[] args) {
        new SpringCusmter2().test();
    }

    public void test() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:application1.xml");
        context.getBean(ParentSuperviseService.class).test("2222");
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
