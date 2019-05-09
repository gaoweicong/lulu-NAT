package com.glwlc.nat.client.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @Author: Gavin
 * @Date: 2019-05-07 10:34
 */
public class NioClient {

    // 分隔符标志位
    private static final String DELIMITER_FP = "$_$";

    private static final int DEFAULT_MAX_LENGTH = 1024 * 1024;

    public void connect(String address, int port) {
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workGroup).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(DEFAULT_MAX_LENGTH,
                                    Unpooled.copiedBuffer(DELIMITER_FP.getBytes())));
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new NatServerChannelHandler());
                            // 写数据时, String转成ByteBuf
                            ch.pipeline().addLast(new StringEncoder());
                        }
                    });
            Channel channel = bootstrap.connect(address, port).sync().channel();
            while (true){
                Thread.sleep(5000);
                System.out.println(System.currentTimeMillis());
                channel.writeAndFlush("HB" + DELIMITER_FP);
            }
//            channel.closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workGroup.shutdownGracefully();
        }
    }
}
