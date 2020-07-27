package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MapStorageTest {

    protected Storage storage = new ListStorage();
    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";

    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_3));
        storage.save(new Resume(UUID_2));
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
        Assert.assertEquals("uuid2", storage.get("uuid2").getUuid());
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() throws Exception {
        storage.get("dummy");
    }

    @Test
    public void update() throws Exception {//не знаю на что проверить этот тест
        storage.update(new Resume("uuid1"));
        Assert.assertEquals("uuid1", storage.get("uuid1").getUuid());
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() throws Exception {
        storage.update(new Resume("dummy"));
    }

    @Test
    public void save() throws Exception {
        storage.save(new Resume("uuid4"));
        Assert.assertEquals("uuid4", storage.get("uuid4").getUuid());
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() throws Exception {
        storage.save(new Resume("uuid1"));
    }

    @Test
    public void delete() throws Exception {
        storage.delete("uuid3");
        Assert.assertEquals(2, storage.size());
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() throws Exception {
        storage.delete("dummy");
    }

    @Test
    public void getAll() throws Exception {
        Resume[] expectedArray;
        Resume r1 = new Resume("uuid1");
        Resume r2 = new Resume("uuid2");
        Resume r3 = new Resume("uuid3");
        expectedArray = new Resume[]{r1, r3, r2};
        Assert.assertArrayEquals(expectedArray, storage.getAll());
    }

}