package com.kuparts.dubbotcc;

import com.kuparts.dubbotcc.commons.exception.TccException;
import com.kuparts.dubbotcc.supervise.api.codec.DefaultCodec;
import com.kuparts.dubbotcc.supervise.propety.InvokeCommand;
import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * Unit test for simple App.
 */
public class CodecTest {

    @Test
    public void test() {
        DefaultCodec codec = new DefaultCodec();
        InvokeCommand command = new InvokeCommand(1, "kryo");
        try {
            ByteBuffer buffer = codec.encodec(command);
            InvokeCommand co2 = codec.decodec(buffer);
            System.out.println(co2.getSid());
        } catch (TccException e) {
            e.printStackTrace();
        }
    }
}
