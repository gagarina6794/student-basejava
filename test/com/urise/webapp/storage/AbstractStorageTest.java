package com.urise.webapp.storage;

import com.urise.webapp.ResumeTestData;
import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public abstract class AbstractStorageTest {
    Storage storage;

    protected static final String path = "C:/Users/Lera/basejava/storage";
    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";


    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(ResumeTestData.fillResume(UUID_1,"Abr"));
        storage.save(ResumeTestData.fillResume(UUID_2,"Bro"));
        storage.save(ResumeTestData.fillResume(UUID_3,"Abron"));
    }

    @Test
    public void size() throws Exception {
        Assert.assertEquals(3, storage.size());
    }

    @Test
    public void clear() throws Exception {
        storage.clear();
        Assert.assertEquals(0, storage.size());
    }

    @Test
    public void get() throws Exception {
        Assert.assertEquals(ResumeTestData.fillResume(UUID_2,"Bro"), storage.get(UUID_2));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() throws Exception {
        storage.get("dummy");
    }

    @Test
    public void update() throws Exception {
        Resume newResume = ResumeTestData.fillResume(UUID_1,"Abr");
        storage.update(newResume);
        Assert.assertEquals(newResume, storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() throws Exception {
        storage.update(ResumeTestData.fillResume("dummy", ""));
    }

    @Test
    public void save() throws Exception {
        Resume newResume = ResumeTestData.fillResume("uuid4","Abr");;
        storage.save(newResume);
        Assert.assertEquals(newResume, storage.get("uuid4"));
        Assert.assertEquals(4, storage.size());
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() throws Exception {
        storage.save(ResumeTestData.fillResume(UUID_1,"Abr"));
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() throws Exception {
        storage.delete(UUID_3);
        Assert.assertEquals(2, storage.size());
        storage.get(UUID_3);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() throws Exception {
        storage.delete("dummy");
    }

    @Test
    public void getAllSorted() throws Exception {
        Resume r1 = ResumeTestData.fillResume(UUID_1,"Abr");
        Resume r2 = ResumeTestData.fillResume(UUID_2,"Bro");
        Resume r3 = ResumeTestData.fillResume(UUID_3,"Abron");
        List<Resume> expectedResumes = Arrays.asList(r1,r3,r2);
        Assert.assertEquals(expectedResumes, storage.getAllSorted());
    }

}