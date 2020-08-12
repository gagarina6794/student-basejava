package com.urise.webapp;

import java.io.File;

public class MainFile {
    public static void main(String[] args) {
        File dir = new File("./src/com/urise/webapp");
        System.out.println(dir.isDirectory());
        String[] list = dir.list();
        if (list != null) {
            for (String name : list){
                System.out.println(name);
            }
        }
    }
}
