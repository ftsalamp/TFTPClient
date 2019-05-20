package com.company;

public class Utils {

    public static byte[] toPrimitive(Byte[] toArray) {
        byte[] bytes = new byte[toArray.length];
        for(int i = 0; i < toArray.length; i++){
            bytes[i] = toArray[i];
        }
        return bytes;
    }
}
