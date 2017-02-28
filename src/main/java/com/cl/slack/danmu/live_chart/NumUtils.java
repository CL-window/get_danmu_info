package com.cl.slack.danmu.live_chart;

/**
 * Created by xiaozhuai on 17/2/19.
 */
public class NumUtils {

    public static int byteArrayToUint16(byte[] b, int offset){
        return  (b[offset+1] & 0xFF)       |
                (b[offset+0] & 0xFF) << 8 ;
    }

    public static int byteArrayToUint32(byte[] b, int offset){
        return  (b[offset+3] & 0xFF)       |
                (b[offset+2] & 0xFF) << 8  |
                (b[offset+1] & 0xFF) << 16 |
                (b[offset+0] & 0xFF) << 24;
    }

    public static void uint16ToByteArray(int num, byte[] b, int offset){
        b[offset+0] = (byte) ((num >> 8)  & 0xFF);
        b[offset+1] = (byte) ( num        & 0xFF);
    }

    public static void uint32ToByteArray(int num, byte[] b, int offset){
        b[offset+0] = (byte) ((num >> 24) & 0xFF);
        b[offset+1] = (byte) ((num >> 16) & 0xFF);
        b[offset+2] = (byte) ((num >> 8)  & 0xFF);
        b[offset+3] = (byte) ( num        & 0xFF);
    }

    public static void reverseBytes(byte[] b, int offset, int len){
        for(int i=0; i<len/2; i++){
            byte tmp = b[offset+len-1-i];
            b[offset+len-1-i] = b[offset+i];
            b[offset+i] = tmp;
        }
    }

}
