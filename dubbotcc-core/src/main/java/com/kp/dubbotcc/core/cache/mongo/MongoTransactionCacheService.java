package com.kp.dubbotcc.core.cache.mongo;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.kp.dubbotcc.commons.exception.TccException;
import com.kp.dubbotcc.commons.utils.Assert;
import com.kp.dubbotcc.core.cache.TransactionCacheService;
import com.kp.dubbotcc.core.cache.TransactionConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.function.Supplier;

/**
 * MongoDb存入数据
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class MongoTransactionCacheService implements TransactionCacheService {


    @Override
    public TransactionConverter initConverter() {
        return new MongoTransactionConverter();
    }

    private static final Logger LOG = LoggerFactory.getLogger(MongoTransactionCacheService.class);
    private static final String COLLECTION_NAME = "dubbo_tcc";

    @Override
    public void save(Supplier<? extends TransactionConverter> convert) {
        MongoTransactionCache cache = null;
        try {
            cache = (MongoTransactionCache) convert.get().convertToCache();
        } catch (TccException tccException) {
            LOG.error(tccException.getCause());
        }
        template.save(cache, COLLECTION_NAME);
    }

    @Override
    public void update(Supplier<? extends TransactionConverter> convert) {
        Assert.notNull(convert);
        MongoTransactionCache cache = null;
        try {
            cache = (MongoTransactionCache) convert.get().convertToCache();
        } catch (TccException tccException) {
            LOG.error(tccException.getCause());
        }
        Query query = new Query();
        query.addCriteria(new Criteria("transId").is(cache.getTransId()));
        Update update = new Update();
        update.set("transId", cache.getTransId());
        update.set("status", cache.getStatus());
        update.set("contents", cache.getContents());
        update.set("startTime", cache.getStartTime());
        template.updateFirst(query, update, MongoTransactionCache.class, COLLECTION_NAME);
    }

    /**
     * mogo对象的数据库操作对象
     */
    @Autowired
    private MongoTemplate template;

    @Override
    public Supplier<TransactionConverter> get(String transId) {
        Assert.notNull(transId);
        Query query = new Query();
        query.addCriteria(new Criteria("transId").is(transId));
        MongoTransactionCache cache = template.findOne(query, MongoTransactionCache.class, COLLECTION_NAME);
        return () -> new MongoTransactionConverter().initByCache(cache);
    }


    /**
     * 清除事务资源..
     *
     * @param transId 事务ID
     */
    @Override
    public void remove(String transId) {
        Assert.notNull(transId);
    }
}
