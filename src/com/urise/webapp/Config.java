package com.urise.webapp;

import com.urise.webapp.storage.SqlStorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Config INSTANCE = new Config();
    protected static final File PROPS =new File("./config/resumes.properties") ;
    //если вставить эту переменную в метод, то что-то не так
    private Properties properties = new Properties();
    private File storageDir;
    String dbUrl;
    String dbPassword;
    String dbUser;
    private SqlStorage sqlStorage;

    public static Config get(){
        return INSTANCE;
    }
//"/Users/lerapegushina/IdeaProjects/basejava/config/resumes.properties")
    private Config(){
        try(InputStream is = Config.class.getResourceAsStream("/resumes.properties")){
            properties.load(is);
            storageDir = new File(properties.getProperty("storage.dir"));
            dbUrl = properties.getProperty("db.url");
            dbUser = properties.getProperty("db.user");
            dbPassword = properties.getProperty("db.password");
            sqlStorage = new SqlStorage(dbUrl,dbUser,dbPassword);

        } catch (IOException e) {
            throw new IllegalStateException("Invalig config file" + PROPS.getAbsolutePath());
        }
    }
    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public String getDbUser() {
        return dbUser;
    }

    public SqlStorage getSqlStorage() {
        return sqlStorage;
    }

    public File getStorageDir() {
        return storageDir;
    }
}
