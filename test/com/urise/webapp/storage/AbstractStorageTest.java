package com.urise.webapp.storage;

import com.urise.webapp.Config;
import com.urise.webapp.ResumeTestData;
import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public abstract class AbstractStorageTest {
    Storage storage;

    protected static final File STORAGE = Config.get().getStorageDir();
    private static final String UUID_1 = UUID.randomUUID().toString();
    private static final String UUID_2 = UUID.randomUUID().toString();
    private static final String UUID_3 = UUID.randomUUID().toString();

    private static final Resume RESUME1 = new Resume(UUID_1,"Abr");
    private static final Resume RESUME2 = new Resume(UUID_2,"Bro");
    private static final Resume RESUME3 = new Resume(UUID_3,"Abron");

    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(RESUME1);
        storage.save(RESUME2);
        storage.save(RESUME3);
    }

    boolean isEqualSize(int expected){
        return expected == storage.size();
    }
    @Test
    public void size() throws Exception {
       Assert.assertTrue(isEqualSize(3));
    }

    @Test
    public void clear() throws Exception {
        storage.clear();
        Assert.assertTrue(isEqualSize(0));
    }

    @Test
    public void get() throws Exception {
        Assert.assertEquals(RESUME2, storage.get(UUID_2));
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
        //Resume newResume = ResumeTestData.fillResume("uuid4","Abr");
        Resume newResume = new Resume("uuid4","Abr");
        storage.save(newResume);
        Assert.assertEquals(newResume, storage.get("uuid4"));
        Assert.assertTrue(isEqualSize(4));
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() throws Exception {
        storage.save(RESUME1);
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() throws Exception {
        storage.delete(UUID_3);
        Assert.assertTrue(isEqualSize(2));
        storage.get(UUID_3);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() throws Exception {
        storage.delete("dummy");
    }

    @Test
    public void getAllSorted() throws Exception {
        Resume r1 = RESUME1;
        Resume r2 = RESUME2;
        Resume r3 = RESUME3;
        List<Resume> expectedResumes = Arrays.asList(r1,r3,r2);
        Assert.assertEquals(expectedResumes, storage.getAllSorted());
    }

}