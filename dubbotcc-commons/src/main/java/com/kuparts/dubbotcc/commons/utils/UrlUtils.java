package com.kuparts.dubbotcc.commons.utils;

import com.alibaba.dubbo.common.URL;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class UrlUtils {
    //标识名称
    private static final String ACTOR = "actor";

    /**
     * 将MAP对象转为
     *
     * @param defaults
     * @return
     */
    public static URL parseURL(Map<String, String> params, Map<String, String> defaults) {
        Assert.notNull(defaults);
        String defProtocol = StringUtils.isBlank(defaults.get("protocol")) ? ACTOR : defaults.get("protocol");
        String defHost = StringUtils.isBlank(defaults.get("host")) ? null : defaults.get("host");
        int defPort = StringUtils.isBlank(defaults.get("port")) ? 0 : Integer.parseInt(defaults.get("port"));
        String defPath = StringUtils.isBlank(defaults.get("path")) ? null : defaults.get("path");
        Assert.notNull(defPath);
        Assert.notNull(defHost);
        Assert.checkConditionArgument(defPort >= 0, "port is error");
        return new URL(defProtocol, defHost, defPort, defPath, params);
    }
}
