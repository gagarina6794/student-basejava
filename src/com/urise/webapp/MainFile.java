package com.urise.webapp;

import java.io.File;

public class MainFile {
    public static void main(String[] args) {
        File dir = new File("./src");
        System.out.println(dir.isDirectory());
      /*  String[] list = dir.list();
        if (list != null) {
            for (String name : list) {
                System.out.println(name);
            }
        }*/

        getAllFilesName(dir);
    }

    private static void getAllFilesName(File file) {
        File[] files = file.listFiles();
        if (files != null) {
            for (File dir : files) {
                System.out.println(dir.getName());
                getAllFilesName(dir);
            }
        }
    }
}
