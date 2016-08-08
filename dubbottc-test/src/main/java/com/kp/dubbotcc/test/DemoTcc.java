package com.kp.dubbotcc.test;


import com.kuparts.dubbotcc.api.TccResponse;
import com.kuparts.dubbotcc.core.spring.TCCC;
import com.kuparts.dubbotcc.core.spring.TcccMethod;

/**
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
@TCCC(SpringCusmter.class)
public class DemoTcc {
    @TcccMethod("test")
    public void test(TccResponse response) {

    }
}
