package me.ele.gateway.tcpproxy.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * Created by liyuanchao on 3/11/18.
 */
public class MyHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception { // (2)
        ByteBuf in = (ByteBuf) msg;
        try {
            while (in.isReadable()) {
                ctx.write(msg); // (1)
                ctx.flush(); // (2)
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception { // (5)
        cause.printStackTrace();
        ctx.close();
    }
}
