package com.glwlc.nat.server.netty.Handler;

import com.glwlc.nat.server.netty.model.NatProtocolMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Author: Gavin
 * @Date: 2019-05-09 10:41
 */
public class MessageEncoderHandler extends MessageToByteEncoder<NatProtocolMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, NatProtocolMessage msg, ByteBuf out) throws Exception {
        if (msg == null || out == null)
            throw new Exception("the message is null");
        out.clear();
        out.writeByte(msg.getVersion());
        out.writeByte(msg.getType());
        out.writeLong(msg.getSessionId());
        out.writeInt(msg.getLength());
        // 序列化去业务层实现
        out.writeBytes(msg.getPayload());
        // 结尾分隔符
        out.writeBytes(NatProtocolMessage.DELIMITER_FP);
    }
}
