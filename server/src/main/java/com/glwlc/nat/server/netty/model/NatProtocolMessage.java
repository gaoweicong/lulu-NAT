package com.glwlc.nat.server.netty.model;

import lombok.Data;

/**
 *
 *
 * @Author: Gavin
 * @Date: 2019-05-08 15:38
 */
@Data
public class NatProtocolMessage {

    // 分隔符标志位
    public static final byte[] DELIMITER_FP = "$_$".getBytes();

    private byte version = 1;

    private byte type;

    private long sessionId;

    private int length;

    private byte[] payload;

    public NatProtocolMessage() { }

    public NatProtocolMessage(byte type, byte[] payload) {
        this(type, payload, 0);
    }

    public NatProtocolMessage(byte type, byte[] payload, long sessionId) {
        this.type = type;
        this.payload = payload;
        this.length = payload.length;
        this.sessionId = sessionId;
    }

    static enum NatProtocolType{

        CONNECTION_UP((byte)1),
        CONNECTION_DOWN((byte)2),
        HEART_BEAT_UP((byte)3),
        HEART_BEAT_DOWN((byte)4),
        UP_STREAM((byte)5),
        DOWN_STREAM((byte)6);

        private byte index;

        private NatProtocolType(byte index) {
            this.index = index;
        }

        public byte getIndex() {
            return index;
        }

        public void setIndex(byte index) {
            this.index = index;
        }
    }
}
