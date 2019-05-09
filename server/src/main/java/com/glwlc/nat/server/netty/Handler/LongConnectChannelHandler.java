package com.glwlc.nat.server.netty.Handler;

import com.glwlc.nat.server.netty.model.LivingChannelCache;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Gavin
 * @Date: 2019-05-07 10:53
 */
public class LongConnectChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private static final Class<LongConnectChannelHandler> LOCK = LongConnectChannelHandler.class;

    private static Map<Integer, LivingChannelCache> cacheChannel = new ConcurrentHashMap<>();

    private static Map<String, SocketChannel> domainChannelMap = new ConcurrentHashMap<>();

    // 心跳标志位
    private static final String HB_FP = "HB";
    // 默认心跳检测时间, 则客户端的心跳发送为 40
    private static final int DEFAULT_INTERVAL_TIME = 60;

    private SocketChannel socketChannel;

    public static SocketChannel getSocketChannel(String domainName){
        if (CollectionUtils.isEmpty(domainChannelMap))
            return null;
        return domainChannelMap.get(domainName);
    }

    public LongConnectChannelHandler(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {

        // 可看出此处是一个装饰类, 真正的实现在 ChannelHandlerContext
        // ByteBuf类似于java的ByteBuffer, 这里是堆外内存 Unsafe包
        byte[] request = new byte[msg.readableBytes()];
        msg.readBytes(request);
        // TODO 客户端连接时应该首先发心跳包
        // 判断心跳包
        handleHeartBeatMsg(ctx, request);
        // TODO 逻辑处理

        System.out.println(new String(request, "UTF-8"));
    }

    private void handleHeartBeatMsg(ChannelHandlerContext ctx, byte[] request) throws Exception {
        if (!HB_FP.equals(new String(request, "UTF-8")))
            return;

        System.out.println(new String(request, "UTF-8"));

        int hashCode;
        // socketChannel心跳初始化
        if (!cacheChannel.containsKey(hashCode = socketChannel.hashCode())) {
            String domainName = null;
            synchronized (LOCK) {
                if (!cacheChannel.containsKey(socketChannel.hashCode())) {
                    // 此处主要考虑, 两个map的新增操作应该是原子性操作
                    do {
                        domainName = RandomStringUtils.randomAlphabetic(6);
                    } while (domainChannelMap.containsKey(domainName));
                    LivingChannelCache livingChannelCache = new LivingChannelCache(socketChannel, domainName);
                    cacheChannel.put(hashCode, livingChannelCache);
                    domainChannelMap.put(domainName, socketChannel);
                    // 简单实现保活机制
                    setSchedule(ctx, livingChannelCache);
                }
            }
            if (StringUtils.isNotEmpty(domainName)) {
                // 首次添加, 通知客户端domainName
//                socketChannel.writeAndFlush("<test> hello domainName"+DELIMITER_FP);
            }
            return;
        }
        // 保活, 更新Schedule
        LivingChannelCache livingChannelCache = cacheChannel.get(hashCode);
        if (livingChannelCache.getScheduledFuture() != null)
            livingChannelCache.getScheduledFuture().cancel(true);
        setSchedule(ctx, livingChannelCache);
    }

    private void setSchedule(ChannelHandlerContext ctx, LivingChannelCache livingChannelCache) {
        livingChannelCache.setScheduledFuture(ctx.executor().schedule(() -> {
            System.out.println("channel close");
            cacheChannel.remove(socketChannel.hashCode());
            domainChannelMap.remove(livingChannelCache.getDomainName());
            socketChannel.close();
        }, DEFAULT_INTERVAL_TIME, TimeUnit.SECONDS));
    }
}
