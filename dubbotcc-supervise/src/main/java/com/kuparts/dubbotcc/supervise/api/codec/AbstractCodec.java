package com.kuparts.dubbotcc.supervise.api.codec;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.kuparts.dubbotcc.core.serializer.KryoSerializer;
import com.kuparts.dubbotcc.core.serializer.ObjectSerializer;

/**
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public abstract class AbstractCodec implements Codec {
    protected static final Logger LOG = LoggerFactory.getLogger(AbstractCodec.class);

    //设置序列化ID
    public ObjectSerializer getSer(String sid) {
        return new KryoSerializer();
        //        Assert.notNull(sid);
//        return ExtensionLoader.getExtensionLoader(ObjectSerializer.class).getLoadedExtension(sid);
    }
}
