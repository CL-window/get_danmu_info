package com.cl.slack.danmu.live_chart;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by xiaozhuai on 17/2/17.
 */
public class SocketClient {

    private String mHost;
    private int mPort;

    private Socket mSocket;
    private OutputStream mOutputStream;
    private InputStream mInputStream;

    public SocketClient(){
        mSocket = new Socket();
    }

    public SocketClient(String host, int port){
        this();
        setAddr(host, port);
    }

    public void setAddr(String host, int port){
        mHost = host;
        mPort = port;
    }

    public boolean connect() {
        try {
            SocketAddress socketAddress = new InetSocketAddress(mHost, mPort);
            mSocket.connect(socketAddress, 3000);
            mSocket.setKeepAlive(true);
            mInputStream = mSocket.getInputStream();
            mOutputStream = mSocket.getOutputStream();
        }catch (Exception e){
            return false;
        }
        return isConnected();
    }

    public boolean isConnected(){
        return mSocket.isConnected();
    }

    public void write(byte[] data, int offset, int len) throws IOException {
        mOutputStream.write(data, offset, len);
    }

    public void write(byte[] data) throws IOException {
        mOutputStream.write(data);
    }

    public int read(byte[] buffer) throws IOException {
        return mInputStream.read(buffer);
    }

    public int read(byte[] buffer, int offset, int len) throws IOException {
        return mInputStream.read(buffer, offset, len);
    }

    public int forceRead(byte[] buffer, int offset, int len) throws IOException {
        int limit = len;
        int count;
        while(true){
            count = read(buffer, offset, limit);
            if(count!=-1){
                limit -= count;
                offset += count;
                if(limit==0){
                    break;
                }
            }
        }
        return len;
    }

    public int readUint16() throws IOException {
        byte[] lenBytes = new byte[2];
        forceRead(lenBytes, 0, 2);
        return NumUtils.byteArrayToUint16(lenBytes, 0);
    }

    public int readUint32() throws IOException {
        byte[] lenBytes = new byte[4];
        forceRead(lenBytes, 0, 4);
        return NumUtils.byteArrayToUint32(lenBytes, 0);
    }

    public void writeUint16(int num) throws IOException {
        byte[] lenBytes = new byte[2];
        NumUtils.uint16ToByteArray(num, lenBytes, 0);
        write(lenBytes);
    }

    public void writeUint32(int num) throws IOException {
        byte[] lenBytes = new byte[4];
        NumUtils.uint32ToByteArray(num, lenBytes, 0);
        write(lenBytes);
    }

    public void skipBytes(int len) throws IOException {
        byte[] tmp = new byte[len];
        forceRead(tmp, 0, len);
    }

    public boolean readAvailable() throws IOException {
        return mInputStream.available() > 0;
    }

    public void disconnected(){
        try {
            mSocket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }




}
