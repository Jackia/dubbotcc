package com.kuparts.dubbotcc.core.dispatch.codec;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.kuparts.dubbotcc.commons.utils.Assert;
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
        Assert.notNull(sid);
        return ExtensionLoader.getExtensionLoader(ObjectSerializer.class).getLoadedExtension(sid);
    }
}
