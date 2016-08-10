package com.kuparts.dubbotcc.core.cache;

import com.alibaba.dubbo.common.extension.SPI;
import com.kuparts.dubbotcc.commons.api.Transaction;
import com.kuparts.dubbotcc.commons.config.TccExtConfig;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 真正的数据操作层...
 * 对于事务操作可以有不同的实现..
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
@SPI("mongo")
public interface TransactionCacheService {
    /**
     * 初始化
     *
     * @param context
     * @param config
     */
    void init(ConfigurableApplicationContext context, TccExtConfig config);

    /**
     * 获取一个事务信息
     *
     * @param transId 事务ID
     * @return 事务转换对象
     */
    Transaction get(String transId);

    /**
     * 存储一个事务
     *
     * @param transaction 事务
     */
    void save(Transaction transaction);

    /**
     * 修改事务对象.
     *
     * @param transaction 事务
     */
    void update(Transaction transaction);

    /**
     * 清除一个事务...
     *
     * @param transId 事务ID
     */
    void remove(String transId);

}
