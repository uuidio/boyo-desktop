package com.hotron.c002fac.tools;

public class HotronJni {
    static {
        System.loadLibrary("hotron_jni");
    }

    public static native int closeSerialNative();

    public static native int disableSimCardHotSwap();

    public static native int enableSimCardHotSwap();

    public static native int openSerialNative(int paramInt);

    public static native String recvSerialNative(int paramInt);

    public static native int sendSerialNative(int paramInt, String paramString);

    public static native String sendSystemCMD(String paramString);

    public static native int switchVoiceChannel(int paramInt1, int paramInt2);

    public static native int testNative();
}

