package com.kuparts.dubbotcc.supervise.support;

import com.alibaba.dubbo.common.URL;
import com.kuparts.dubbotcc.commons.config.TccExtConfig;
import com.kuparts.dubbotcc.commons.utils.Assert;
import com.kuparts.dubbotcc.supervise.propety.Context;

/**
 * 负责启动监管系统,对各子系统的事务管理,
 * 自动接管,上线注册待
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class ParentSuperviseService {

    private TccExtConfig config;

    private boolean isRuning = false;

    public ParentSuperviseService(TccExtConfig config) {
        this.config = config;
    }

    /**
     * 启动事务监听
     */
    public void start() {
        Assert.notNull(config);
        if (!isRuning) {
            Context.getContext().setPort(config.getPort());
            URL zookUrl = URL.valueOf(config.getZookurl());
            Context.getContext().setRigisterUrl(zookUrl);
            Context.getContext().setSid(config.getSerializer());
        }
    }
}
