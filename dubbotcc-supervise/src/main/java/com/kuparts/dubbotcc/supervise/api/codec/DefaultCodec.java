package com.kuparts.dubbotcc.supervise.api.codec;

import com.kuparts.dubbotcc.commons.exception.TccException;
import com.kuparts.dubbotcc.core.serializer.ObjectSerializer;
import com.kuparts.dubbotcc.core.serializer.SerializerFactory;
import com.kuparts.dubbotcc.supervise.propety.InvokeCommand;

import java.nio.ByteBuffer;

/**
 * 通知信息编码
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class DefaultCodec extends AbstractCodec {
    @Override
    public ByteBuffer encodec(InvokeCommand command) throws TccException {
        int length = 4;
        ObjectSerializer serializer = getSer(command.getSid());
        int sid = SerializerFactory.getSerId(command.getSid());
        length += 4;
        try {
            byte datas[] = serializer.serialize(command);
            length += datas.length;
            ByteBuffer buffer = ByteBuffer.allocate(4 + length);
            buffer.putInt(length);
            buffer.putInt(sid);
            buffer.putInt(datas.length);
            buffer.put(datas);
            buffer.flip();
            return buffer;
        } catch (TccException e) {
            LOG.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public InvokeCommand decodec(ByteBuffer buffer) throws TccException {
        int length = buffer.getInt();
        int sid = buffer.getInt();
        int hlength = buffer.getInt();
        String sidStr = SerializerFactory.getStrs(sid);
        ObjectSerializer serializer = getSer(sidStr);
        byte[] bytes = new byte[hlength];
        buffer.get(bytes);
        try {
            return serializer.deSerialize(bytes, InvokeCommand.class);
        } catch (TccException e) {
            LOG.error(e.getMessage());
            throw e;
        }
    }
}
