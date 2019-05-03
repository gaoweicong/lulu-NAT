package com.glwlc.nat.server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

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
                    .option(ChannelOption.SO_BACKLOG, 1024)
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
        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            // 类似于流式编程的管道, 可定义各种解析处理器
            socketChannel.pipeline().addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                @Override
                protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                    // 可看出此处是一个装饰类, 真正的实现在 ChannelHandlerContext
                    // ByteBuf类似于java的ByteBuffer, 这里是堆外内存 Unsafe包
                    byte[] request = new byte[msg.readableBytes()];
                    msg.readBytes(request);
                    System.out.println(new String(request, "UTF-8"));
                }
            });
        }
    }


}
