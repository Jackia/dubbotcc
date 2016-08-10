package com.kuparts.dubbotcc.core.cache.mongo;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.kuparts.dubbotcc.commons.api.Transaction;
import com.kuparts.dubbotcc.commons.exception.TccRuntimeException;
import com.kuparts.dubbotcc.commons.utils.Assert;
import com.kuparts.dubbotcc.core.cache.AbstractTransactionCacheService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * MongoDb存入数据
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class MongoTransactionCacheService extends AbstractTransactionCacheService {

    /**
     * mogo对象的数据库操作对象
     */
    private MongoTemplate template;


    private static final Logger LOG = LoggerFactory.getLogger(MongoTransactionCacheService.class);
    private static final String COLLECTION_NAME = "dubbo_tcc";
    private static final String COLLECTION_NAME_HISTORY = "dubbo_tcc_history";

    @Override
    protected void init0(Object obj) {
        template = (MongoTemplate) obj;
    }

    @Override
    public void save(Transaction transaction) {
        Assert.notNull(transaction);
        MongoTransactionCache cache;
        try {
            cache = (MongoTransactionCache) super.converter.convertToCache(transaction);
        } catch (Exception tccException) {
            LOG.error(tccException.getCause());
            throw new TccRuntimeException("save transaction error");
        }
        template.save(cache, COLLECTION_NAME);
    }

    @Override
    public void update(Transaction transaction) {
        Assert.notNull(transaction);
        MongoTransactionCache cache;
        try {
            cache = (MongoTransactionCache) super.converter.convertToCache(transaction);
        } catch (Exception tccException) {
            LOG.error(tccException.getCause());
            throw new TccRuntimeException("update transaction error");
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

    @Override
    public Transaction get(String transId) {
        Assert.notNull(transId);
        Query query = new Query();
        query.addCriteria(new Criteria("transId").is(transId));
        MongoTransactionCache cache = template.findOne(query, MongoTransactionCache.class, COLLECTION_NAME);
        try {
            return super.converter.convertByCache(cache);
        } catch (Exception e) {
            throw new TccRuntimeException("convert catch to Transaction error");
        }
    }

    /**
     * 清除事务资源..
     * 不是物理上删除,只是放入历史数据中
     *
     * @param transId 事务ID
     */
    @Override
    public void remove(String transId) {
        Assert.notNull(transId);
        Query query = new Query();
        query.addCriteria(new Criteria("transId").is(transId));
        MongoTransactionCache cache = template.findOne(query, MongoTransactionCache.class, COLLECTION_NAME);
        template.save(cache, COLLECTION_NAME_HISTORY);
        //查询事务数据
        template.remove(query, COLLECTION_NAME);
    }

    @Override
    protected String getCacheName() {
        return "mongo";
    }


}
