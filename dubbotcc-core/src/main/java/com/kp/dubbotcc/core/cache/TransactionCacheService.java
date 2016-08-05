package com.kp.dubbotcc.core.cache;

import com.alibaba.dubbo.common.extension.SPI;

import java.util.function.Supplier;

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
     * 获取一个事务信息
     *
     * @param transId 事务ID
     * @return 事务转换对象
     */
    Supplier<TransactionConverter> get(String transId);

    /**
     * 存储一个事务
     *
     * @param convert 事务转换对象
     */
    void save(Supplier<? extends TransactionConverter> convert);

    /**
     * 修改事务对象.
     *
     * @param convert 转换对象
     */
    void update(Supplier<? extends TransactionConverter> convert);

    /**
     * 清除一个事务...
     *
     * @param transId 事务ID
     */
    void remove(String transId);

    /**
     * 初始化一下转换器</pr>
     * 适用于KEY/Value结构
     * KEY,transID
     * Value,序列化对象
     */
    default TransactionConverter initConverter() {
        return new DefaultTransactionConverter();
    }
}
