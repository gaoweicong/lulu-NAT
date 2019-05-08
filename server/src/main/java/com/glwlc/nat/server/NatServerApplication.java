package com.glwlc.nat.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @Author: Gavin
 * @Date: 2019-04-24 14:13
 */

@EnableWebMvc
@SpringBootApplication
public class NatServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(NatServerApplication.class, args);
//        NioServer nioServer = new NioServer();
//        nioServer.bindAndStart(8888);
    }
}
