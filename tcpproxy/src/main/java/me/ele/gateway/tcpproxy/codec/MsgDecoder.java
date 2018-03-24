package me.ele.gateway.tcpproxy.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import me.ele.gateway.tcpproxy.bean.MyMessage;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by liyuanchao on 3/24/18.
 */
public class MsgDecoder extends ByteToMessageDecoder {
    int packLength = 0;
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }
        if (packLength == 0) {
            packLength = in.readInt();
        }

        ByteBuffer buf = ByteBuffer.allocate(packLength);
        in.readBytes(buf);
        buf.flip(); packLength = 0;
        MyMessage msg = new MyMessage();
        msg.setMessage("hello");
        msg.setMsgId(1l);
        out.add(msg);
    }
}
