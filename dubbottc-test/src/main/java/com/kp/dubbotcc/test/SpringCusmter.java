package com.kp.dubbotcc.test;

import com.kuparts.dubbotcc.api.Transaction;
import com.kuparts.dubbotcc.core.cache.TransactionCacheService;
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
        context.getBeanFactory();
        Transaction transaction = new Transaction();
        TransactionCacheService service = context.getBean(TransactionCacheService.class);
        service.save(transaction);
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
