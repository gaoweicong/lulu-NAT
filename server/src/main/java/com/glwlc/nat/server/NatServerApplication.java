package com.glwlc.nat.server;

import com.glwlc.nat.server.netty.NioClient;
import com.glwlc.nat.server.netty.NioServer;

/**
 * @Author: Gavin
 * @Date: 2019-04-24 14:13
 */

//@EnableWebMvc
//@SpringBootApplication
public class NatServerApplication {
    public static void main(String[] args) {
//        SpringApplication.run(NatServerApplication.class, args);

        // 启动server
        new Thread(() -> {
            NioServer nioServer = new NioServer();
            nioServer.bindAndStart(8080);
        }).start();

        // sleep 3s
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 启动client
        NioClient nioClient = new NioClient();
        nioClient.connect("127.0.0.1", 8080);
    }
}
