package com.wuyou.utils;


import java.io.BufferedReader;
import java.io.InputStreamReader;

public class DrawUtil {
    public static void main(String[] args) {
        String saveImageLocation = "/Users/wuyou/test/aaa.png";
        try {
            String[] args1 = new String[]{"python", "/Users/wuyou/PycharmProjects/AI/ImageTest.py", "/Users/wuyou/other/coolq-robot/src/resourcesData/poker/", "/Users/wuyou/other/coolq-robot/src/resourcesData/poker_comp/a1_a2.png", "a1", "a2"};
            Process proc = Runtime.getRuntime().exec(args1);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            System.out.println(in);
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            proc.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
