package com.glwlc.nat.server.netty.model;

import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.ScheduledFuture;
import lombok.Data;

/**
 * @Author: Gavin
 * @Date: 2019-05-07 14:14
 */
@Data
public class LivingChannelCache {

    private SocketChannel socketChannel;

    private ScheduledFuture scheduledFuture;

    private String domainName;

    public LivingChannelCache(SocketChannel socketChannel, String domainName) {
        this.socketChannel = socketChannel;
        this.domainName = domainName;
    }
}
