package com.glwlc.nat.client.netty;

/**
 * @Author: Gavin
 * @Date: 2019-05-07 17:04
 */
public class MainTest {
    public static void main(String[] args) {
        NioClient nioClient = new NioClient();
        nioClient.connect("abc.glwlc.top", 8888);
    }
}
