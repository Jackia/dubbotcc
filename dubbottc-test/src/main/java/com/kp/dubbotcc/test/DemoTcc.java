package com.kp.dubbotcc.test;


import com.kuparts.dubbotcc.commons.api.TccResponse;
import com.kuparts.dubbotcc.commons.bean.TCCC;
import com.kuparts.dubbotcc.commons.bean.TcccMethod;

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
