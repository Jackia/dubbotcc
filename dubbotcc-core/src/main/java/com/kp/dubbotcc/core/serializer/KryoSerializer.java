/*
 * Copyright (c) 2016.
 * kupats(sz)
 * www.kuparts.com.
 * Created By chenbin on 16-6-14 上午10:53.
 */

package com.kp.dubbotcc.core.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.kp.dubbotcc.commons.exception.TccException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 通过kryo 序列化对象
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class KryoSerializer implements ObjectSerializer {
    /**
     * 序列化
     *
     * @param obj 需要序更列化的对象
     * @return 序列化后的byte 数组
     * @throws TccException
     */
    @Override
    public byte[] serialize(Object obj) throws TccException {
        byte[] bytes = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            //获取kryo对象
            Kryo kryo = new Kryo();
            Output output = new Output(outputStream);
            kryo.writeObject(output, obj);
            bytes = output.toBytes();
            output.flush();
        } catch (Exception ex) {
            throw new TccException("kryo serialize error" + ex.getMessage());
        }finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {

            }
        }
        return bytes;
    }

    /**
     * 反序列化
     *
     * @param param 需要反序列化的byte []
     * @return 序列化对象
     * @throws TccException
     */
    @Override
    public <T> T deSerialize(byte[] param,Class<T> clazz) throws TccException {
        T object = null;
        ByteArrayInputStream inputStream = new ByteArrayInputStream(param);
        try {

            Kryo kryo = new Kryo();
            Input input = new Input(inputStream);
            object = kryo.readObject(input,clazz);
            input.close();
        }catch (Exception e){
            throw new TccException("kryo deSerialize error" + e.getMessage());
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                
            }
        }
        return object;
    }
}
