package org.example;

import java.net.MalformedURLException;
import java.net.URL;

public class Main {

    public static void main(String[] args) {
        String[] ips;
        try {
            ips = Utils.getRunningVMsIps();

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }


    }
}