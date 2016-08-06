/*
 * Copyright (c) 2016.
 * kupats(sz)
 * www.kuparts.com.
 * Created By chenbin on 16-6-14 上午10:48.
 */

package com.kuparts.dubbotcc.core.serializer;

import com.alibaba.dubbo.common.extension.SPI;
import com.kuparts.dubbotcc.commons.exception.TccException;

/**
 * 对象序列化父类
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
@SPI("kryo")
public interface ObjectSerializer {
    /**
     * 序列化对象
     *
     * @param obj 需要序更列化的对象
     * @return byte []
     * @throws TccException
     */
    byte[] serialize(Object obj) throws TccException;

    /**
     * 反序列化对象
     *
     * @param param 需要反序列化的byte []
     * @return 对象
     * @throws TccException
     */
    <T> T deSerialize(byte[] param, Class<T> clazz) throws TccException;
}
