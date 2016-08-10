package com.kp.dubbotcc;

import com.kuparts.dubbotcc.commons.utils.UrlUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Unit test for simple App.
 */
public class AppTest {
    @Test
    public void TestUrl() {
        Map<String, String> params = new HashMap<>();
        params.put("username", "1212212");
        params.put("password", "1212212");
        Map<String, String> defauls = new HashMap<>();
        defauls.put("protocol", "actor");
        defauls.put("port", "9090");
        defauls.put("host", "127.0.0.1");
        defauls.put("path", "check-api");
        String url = UrlUtils.parseURL(params, defauls).toFullString();
        System.out.println(url);
    }
}
