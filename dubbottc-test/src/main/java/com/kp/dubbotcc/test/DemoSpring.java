package com.kp.dubbotcc.test;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
@Component
public class DemoSpring {
    public void print() {
        System.out.println("jjjjjjjjjjjjjjjjj");
    }

    @PostConstruct
    public void init() {
        System.out.println("iiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
    }
}
