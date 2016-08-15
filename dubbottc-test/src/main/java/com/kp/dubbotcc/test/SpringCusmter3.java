package com.kp.dubbotcc.test;

import com.kuparts.dubbotcc.core.dispatch.support.ParentSuperviseService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class SpringCusmter3 {
    public static void main(String[] args) {
        new SpringCusmter3().test();
    }

    public void test() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:application2.xml");
        context.getBean(ParentSuperviseService.class).test("3333");
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
