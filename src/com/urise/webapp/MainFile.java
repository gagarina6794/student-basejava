package com.urise.webapp;

import java.io.File;

public class MainFile {
    public static void main(String[] args) {
        File dir = new File("./src");
        System.out.println(dir.isDirectory());
        getAllFilesName(dir, 0);
    }

    private static void getAllFilesName(File file, int spaceCount) {
        File[] files = file.listFiles();
        if (files != null) {
            for (File dir : files) {
                for(int i = 0; i<spaceCount; i++)
                    System.out.print("  ");
                System.out.println(dir.getName());
                getAllFilesName(dir, spaceCount+1);
            }
        }
    }
}
