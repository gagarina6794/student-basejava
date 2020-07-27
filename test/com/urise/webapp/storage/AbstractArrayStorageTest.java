package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.Test;

import static org.junit.Assert.fail;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest {

    public AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }

    @Test(expected = StorageException.class)
    public void storageOverflow() throws Exception {
        int currentSize = storage.size();
        try {
            for (int i = currentSize; i < 10_000; i++) {
                storage.save(new Resume());
            }
        } catch (ExistStorageException ex) {
            fail("Throwed exeption before main test");
        }
        storage.save(new Resume());
    }
}