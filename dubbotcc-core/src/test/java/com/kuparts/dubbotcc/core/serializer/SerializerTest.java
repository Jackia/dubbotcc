package com.kuparts.dubbotcc.core.serializer;

import com.kuparts.dubbotcc.commons.exception.TccException;
import com.kuparts.dubbotcc.commons.utils.DateUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class SerializerTest {
    @Test
    public void testJava() {
        long start = DateUtils.nowTimeMillis();
        Demo demo = new Demo();
        demo.setId("11111111111111111");
        demo.setName("11111111111111111");
        demo.setUser("11111111111111111");
        JavaSerializer javaSerializer = new JavaSerializer();
        try {
            byte[] bytes = javaSerializer.serialize(demo);
            System.out.println(bytes.length);
            Demo demo1 = javaSerializer.deSerialize(bytes, Demo.class);
            Assert.assertEquals("11111111111111111", demo1.getName());
            long end = DateUtils.nowTimeMillis();
            System.out.println(end-start);
        } catch (TccException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testKryo() {
        long start = DateUtils.nowTimeMillis();
        Demo demo = new Demo();
        demo.setId("11111111111111111");
        demo.setName("11111111111111111");
        demo.setUser("11111111111111111");
        KryoSerializer kryoSerializer = new KryoSerializer();
        try {
            byte[] bytes = kryoSerializer.serialize(demo);
            System.out.println(bytes.length);
            Demo demo1 = kryoSerializer.deSerialize(bytes, Demo.class);
            Assert.assertEquals("11111111111111111", demo1.getName());
            long end = DateUtils.nowTimeMillis();
            System.out.println(end-start);
        } catch (TccException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHessian() {
        long start = DateUtils.nowTimeMillis();
        Demo demo = new Demo();
        demo.setId("11111111111111111");
        demo.setName("11111111111111111");
        demo.setUser("11111111111111111");
        HessianSerializer kryoSerializer = new HessianSerializer();
        try {
            byte[] bytes = kryoSerializer.serialize(demo);
            System.out.println(bytes.length);
            Demo demo1 = kryoSerializer.deSerialize(bytes, Demo.class);
            Assert.assertEquals("11111111111111111", demo1.getName());
            long end = DateUtils.nowTimeMillis();
            System.out.println(end-start);
        } catch (TccException e) {
            e.printStackTrace();
        }
    }
}
