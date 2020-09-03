package com.urise.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Config INSTANCE = new Config();
    protected static final File PROPS =new File(".\\config\\resumes.properties") ;

    private Properties properties = new Properties();
    private File storageDir;

    public static Config get(){
        return INSTANCE;
    }

    private Config(){
        try(InputStream is = new FileInputStream(PROPS)){
            properties.load(is);
            storageDir = new File(properties.getProperty("storage.dir"));
        } catch (IOException e) {
            throw new IllegalStateException("Invalig config file" + PROPS.getAbsolutePath());
        }
    }

    public File getStorageDir() {
        return storageDir;
    }
}
