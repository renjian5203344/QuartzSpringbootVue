package com.yizhan.javatask.main;

public class YiZhanJavaTask2 {

    public static void main(String[] args) {
        if (args.length<2){
            System.out.println("at least two twoparam <param1 ,param2> ");
            return;

        }
        String param1 = args[0];
        String param2 = args[1];
        System.out.println("yizhan task excute == param1: " + param1 + "param2: " + param2);

    }

}
