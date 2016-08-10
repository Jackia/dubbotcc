package com.kuparts.dubbotcc.core.rollback.task;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.kuparts.dubbotcc.commons.api.CompensationCallback;
import com.kuparts.dubbotcc.commons.api.TccResponse;
import com.kuparts.dubbotcc.commons.utils.Assert;
import com.kuparts.dubbotcc.core.cache.TransactionCacheService;
import com.kuparts.dubbotcc.core.major.TransactionFilter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 清除成功的缓存数据..
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
@DefaultTask
public class ClearCacheCallback implements CompensationCallback {
    private static final Logger LOG = LoggerFactory.getLogger(TransactionFilter.class);
    @Autowired
    TransactionCacheService cacheService;

    @Override
    public void callback(TccResponse response) {
        Assert.notNull(response);
        if (response.isSuccessful()) {
            cacheService.remove(response.getTransId());
            LOG.info("清除缓存数据.." + response.getTransId());
        }
    }
}
