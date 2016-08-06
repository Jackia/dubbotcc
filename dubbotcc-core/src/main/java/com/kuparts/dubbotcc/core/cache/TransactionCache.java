package com.kuparts.dubbotcc.core.cache;

import java.io.Serializable;

/**
 * 将事务对象保存到缓存对象
 * 可以是redis,ecache,mongo
 * 这个类只需要key/value节构存储
 * 如果有需要,自己可以继承这个类进行扩展
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class TransactionCache implements Serializable {
    private static final long serialVersionUID = 69273420105035451L;
    /**
     * 事务id
     */
    private String transId;
    /**
     * 序列化后的二进制信息
     */
    private byte[] contents;

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public byte[] getContents() {
        return contents;
    }

    public void setContents(byte[] contents) {
        this.contents = contents;
    }
}
