package com.example.sockettestclient.objects;

import java.nio.ByteBuffer;

public class Header {
    public static final int HEADER_LENGTH = 10;
    public char code;
    public int length;
    public String hash;

    public byte[] getHeader(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(Header.HEADER_LENGTH);
        byteBuffer.putChar(this.code);
        byteBuffer.putInt(this.length);
        byteBuffer.put(this.hash.getBytes());
        byteBuffer.position(0);
        return byteBuffer.array();
    }

    public Header(byte[] headerData){
        ByteBuffer byteBuffer = ByteBuffer.allocate(Header.HEADER_LENGTH);
        byteBuffer.put(headerData);
        byteBuffer.position(0);
        this.code = byteBuffer.getChar();
        this.length = byteBuffer.getInt();
        byte[] hashByte = new byte[4];
        byteBuffer.get(hashByte);
        this.hash = new String(hashByte);
    }

    public Header(){

    }
}
