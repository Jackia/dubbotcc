package com.kuparts.dubbotcc.core.spring;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

/**
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class SpringLoadTest {

    @Test
    public void test() {
        String va = "com.kp.api.cccccc";
        List<String> result = Splitter.on(".").limit(3).splitToList(va);
        System.out.println(Joiner.on(".").join(result.subList(0,2)));


    }
}
