package com.kuparts.dubbotcc.core.cache.config;

import com.google.common.base.Splitter;
import com.kuparts.dubbotcc.commons.config.TccExtConfig;
import com.kuparts.dubbotcc.commons.utils.Assert;
import com.kuparts.dubbotcc.core.cache.TransactionConverter;
import com.kuparts.dubbotcc.core.cache.mongo.MongoTransactionConverter;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * mongo初始化
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class MongoCacheApplicationContext extends AbstractCacheApplicationContext {

    @Override
    public Object init$(TccExtConfig config) throws Exception {
        Assert.notNull(config.getMongoDbName());
        Assert.notNull(config.getMongoDbUrl());
        Assert.notNull(config.getMongoUserName());
        Assert.notNull(config.getMongoUserPwd());
        MongoClientFactoryBean clientFactoryBean = getMongoClientFactoryBean(config);
        clientFactoryBean.afterPropertiesSet();
        MongoTemplate template = new MongoTemplate(clientFactoryBean.getObject(), config.getMongoDbName());
        template.setWriteConcern(WriteConcern.NORMAL);
        registerBean(MongoTemplate.class.getName(), template);
        return template;
    }

    @Override
    protected TransactionConverter initConvert() {
        return new MongoTransactionConverter();
    }

    /**
     * 生成mongoClientFacotryBean
     *
     * @param config 配置信息
     * @return bean
     */
    private MongoClientFactoryBean getMongoClientFactoryBean(TccExtConfig config) {
        MongoClientFactoryBean clientFactoryBean = new MongoClientFactoryBean();
        MongoCredential credential = MongoCredential.createMongoCRCredential(config.getMongoUserName(),
                config.getMongoDbName(),
                config.getMongoUserPwd().toCharArray());
        clientFactoryBean.setCredentials(new MongoCredential[]{
                credential
        });
        List<String> urls = Splitter.on(",").trimResults().splitToList(config.getMongoDbUrl());
        ServerAddress[] sds = new ServerAddress[urls.size()];
        for (int i = 0; i < sds.length; i++) {
            List<String> adds = Splitter.on(":").trimResults().splitToList(urls.get(i));
            InetSocketAddress address = new InetSocketAddress(adds.get(0), Integer.parseInt(adds.get(1)));
            sds[i] = new ServerAddress(address);
        }
        clientFactoryBean.setReplicaSetSeeds(sds);
        return clientFactoryBean;
    }
}
