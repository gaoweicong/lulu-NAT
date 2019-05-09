package com.glwlc.nat.server.service.impl;

import com.glwlc.nat.server.constant.ErrorCode;
import com.glwlc.nat.server.netty.Handler.LongConnectChannelHandler;
import com.glwlc.nat.server.service.NatConnectHandler;
import io.netty.channel.socket.SocketChannel;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: Gavin
 * @Date: 2019-05-08 14:36
 */
@Service
public class NatConnectHandlerImpl implements NatConnectHandler {
    @Override
    public byte[] handlerNat(HttpServletRequest request, String domainName) {

        SocketChannel socketChannel = LongConnectChannelHandler.getSocketChannel(domainName);
        if (socketChannel == null)
            return ErrorCode.DOMAIN_NAME_NOT_FOUND;


        return new byte[0];
    }
}
