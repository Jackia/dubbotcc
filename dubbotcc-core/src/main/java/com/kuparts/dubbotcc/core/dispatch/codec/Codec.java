package com.kuparts.dubbotcc.core.dispatch.codec;

import com.kuparts.dubbotcc.core.dispatch.propety.InvokeCommand;

import java.nio.ByteBuffer;

/**
 * 网络传输,密码与解码
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public interface Codec {
    /**
     * 解码
     *
     * @param command
     * @return
     */
    ByteBuffer encodec(InvokeCommand command) throws Exception;

    /**
     * 加码
     *
     * @param buffer
     * @return
     */
    InvokeCommand decodec(ByteBuffer buffer) throws Exception;
}
