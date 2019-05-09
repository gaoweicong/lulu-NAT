package com.glwlc.nat.server.netty.Handler;

import com.glwlc.nat.server.netty.model.NatProtocolMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * @Author: Gavin
 * @Date: 2019-05-09 11:18
 */
public class MessageDecoderHandler extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List out) throws Exception {

        if (msg == null || out == null)
            throw new Exception("the message is null");

        NatProtocolMessage natProtocolMessage = new NatProtocolMessage();
        natProtocolMessage.setVersion(msg.readByte());
        natProtocolMessage.setType(msg.readByte());
        natProtocolMessage.setSessionId(msg.readLong());
        int length = msg.readInt();
        natProtocolMessage.setLength(length);

        if (msg.readableBytes()!=length)
            throw new Exception("data lost because net");
        byte[] body = new byte[length];
        msg.readBytes(body);
        natProtocolMessage.setPayload(body);
        // 加入
        out.add(natProtocolMessage);
    }
}
