package com.cl.slack.danmu.live_chart;

import java.nio.charset.Charset;
import java.security.MessageDigest;

/**
 * Created by xiaozhuai on 17/2/20.
 */
public class TextEncoder {

    private static char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();
    private static byte[] codes = new byte[256];

    static {
        for (int i = 0; i < 256; i++)
            codes[i] = -1;
        for (int i = 'A'; i <= 'Z'; i++)
            codes[i] = (byte) (i - 'A');
        for (int i = 'a'; i <= 'z'; i++)
            codes[i] = (byte) (26 + i - 'a');
        for (int i = '0'; i <= '9'; i++)
            codes[i] = (byte) (52 + i - '0');
        codes['+'] = 62;
        codes['/'] = 63;
    }

    public static String base64Encode(String str)
    {

        byte[] data = str.getBytes();

        char[] out = new char[((data.length + 2) / 3) * 4];

        for (int i = 0, index = 0; i < data.length; i += 3, index += 4)
        {
            boolean quad = false;
            boolean trip = false;
            int val = (0xFF & (int) data[i]);
            val <<= 8;
            if ((i + 1) < data.length)
            {
                val |= (0xFF & (int) data[i + 1]);
                trip = true;
            }
            val <<= 8;
            if ((i + 2) < data.length)
            {
                val |= (0xFF & (int) data[i + 2]);
                quad = true;
            }
            out[index + 3] = alphabet[(quad ? (val & 0x3F) : 64)];
            val >>= 6;
            out[index + 2] = alphabet[(trip ? (val & 0x3F) : 64)];
            val >>= 6;
            out[index + 1] = alphabet[val & 0x3F];
            val >>= 6;
            out[index + 0] = alphabet[val & 0x3F];
        }
        return new String(out, 0, out.length);
    }

    public static String base64Decode(String str)
    {

        char[] data = str.toCharArray();

        int len = ((data.length + 3) / 4) * 3;
        if(data.length > 0 && data[data.length - 1] == '=')
            --len;
        if(data.length > 1 && data[data.length - 2] == '=')
            --len;
        byte[] out = new byte[len];
        int shift = 0;
        int accum = 0;
        int index = 0;
        for(int ix = 0; ix < data.length; ix++)
        {
            int value = codes[data[ix] & 0xFF];
            if(value >= 0)
            {
                accum <<= 6;
                shift += 6;
                accum |= value;
                if(shift >= 8)
                {
                    shift -= 8;
                    out[index++] = (byte)((accum >> shift) & 0xff);
                }
            }
        }
        if(index != out.length)
            throw new Error("miscalculated data length!");
        return new String(out, 0, out.length);
    }

    public static String md5Str(String pwd){
        //用于加密的字符
        char md5String[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            byte[] btInput = pwd.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {   //  i = 0
                byte byte0 = md[i];  //95
                str[k++] = md5String[byte0 >>> 4 & 0xf];    //    5
                str[k++] = md5String[byte0 & 0xf];   //   F
            }
            return new String(str);
        } catch (Exception e) {
            return "";
        }
    }

}
