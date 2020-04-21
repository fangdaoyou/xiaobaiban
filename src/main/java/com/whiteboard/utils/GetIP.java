package com.whiteboard.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class GetIP {
    public static void main(String[] args) throws UnknownHostException {
        String addr = InetAddress.getLocalHost().getHostAddress();
        System.out.println(addr);
    }
}
