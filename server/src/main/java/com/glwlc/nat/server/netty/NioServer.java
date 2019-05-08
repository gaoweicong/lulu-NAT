package com.glwlc.nat.server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @Author: Gavin
 * @Date: 2019-04-29 11:49
 */
public class NioServer {

    public void bindAndStart(int port) {
        // 创建与客户端连接的回调线程池, 此处可以这样理解, 对于tcp连接就是3次握手, 每次响应用
        // 回调接口去处理
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        // 创建channel读写的回调线程池
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            // 统一配置引导类, 链式编程
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup)
                    // 定义channel为nio
                    .channel(NioServerSocketChannel.class)
                    // 配置
                    .option(ChannelOption.SO_BACKLOG, 1024*1024)
                    .option(ChannelOption.TCP_NODELAY, true)
                    // 2小时没有数据传输主动发一个, 冗余机制
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // channel处理类
                    .childHandler(new ChildChannelHandler());
            // 此处同步绑定端口, 阻塞方法
            // TODO 具体实现未知
            ChannelFuture sync = serverBootstrap.bind(port).sync();
            sync.channel().closeFuture().sync();
        } catch (Throwable e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

        private static final int DEFAULT_MAX_LENGTH = 1024 * 1024;

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {

            // 1024是读取数据的最大长度, 这是防止分隔符在传输中丢失导致内存溢出
            socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(DEFAULT_MAX_LENGTH,
                    Unpooled.copiedBuffer(LongConnectChannelHandler.DELIMITER_FP.getBytes())));
            // 类似于流式编程的管道, 可定义各种解析处理器
            socketChannel.pipeline().addLast(new LongConnectChannelHandler(socketChannel));
            // 写数据时, String转成ByteBuf
            socketChannel.pipeline().addLast(new StringEncoder());
        }


    }


}
