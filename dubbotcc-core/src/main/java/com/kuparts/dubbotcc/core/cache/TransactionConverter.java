package com.kuparts.dubbotcc.core.cache;


import com.kuparts.dubbotcc.api.Transaction;
import com.kuparts.dubbotcc.core.major.BeanServiceUtils;
import com.kuparts.dubbotcc.core.serializer.ObjectSerializer;

/**
 * 将事务对象转换为存入缓存对象
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public abstract class TransactionConverter<C extends TransactionCache> {
    private Transaction transaction;

    private ObjectSerializer serializer;

    private C transactionCache;

    protected Transaction getTransaction() {
        return transaction;
    }

    protected ObjectSerializer getSerializer() {
        return serializer;
    }

    protected C getTransactionCache() {
        return transactionCache;
    }

    /**
     * 将transaction对象转换为可存入缓存对象
     *
     * @return
     */
    public abstract C convertToCache() throws Exception;

    public abstract Transaction convertByCache() throws Exception;

    /**
     * 初始化转换信息
     * 从事务对象转换为缓存对象
     *
     * @param transaction 事务对象
     * @return 转换器
     */
    public TransactionConverter initToCache(Transaction transaction) {
        this.transaction = transaction;
        serializer = BeanServiceUtils.getInstance().getBean(ObjectSerializer.class);
        return this;
    }

    /**
     * 初始化转换对象..
     * 从缓存对像转换为事务对象
     *
     * @param cache 缓存对象
     * @return 转换器
     */
    public TransactionConverter initByCache(C cache) {
        transactionCache = cache;
        serializer = BeanServiceUtils.getInstance().getBean(ObjectSerializer.class);
        return this;
    }
}
