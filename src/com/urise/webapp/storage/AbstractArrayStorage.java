package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage extends AbstractStorage {
    protected static final int STORAGE_LIMIT = 10_000;
    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int count = 0;

    protected void clearStorage() {
        Arrays.fill(storage, 0, count, null);
        count = 0;
    }

    protected Resume getFromStorage(int goalIndex) {
        return storage[goalIndex];
    }

    protected void updateInStorage(Resume resume, int goalIndex) {
        storage[goalIndex] = resume;
    }

    protected void saveInStorage(Resume resume, int goalIndex) {
        if (count < storage.length) {
            saveResume(resume, goalIndex);
            count++;
        } else {
            throw new StorageException("Storage overflow", resume.getUuid());
        }
    }

    protected void deleteFromStorage(int goalIndex) {
        deleteResume(goalIndex);
        storage[count--] = null;
    }

    protected Resume[] getAllStorage() {
        return Arrays.copyOfRange(storage, 0, count);
    }

    protected int getSize(){
        return count;
    }

    protected abstract void deleteResume(int goalIndex);

    protected abstract void saveResume(Resume resume, int saveIndex);
}
