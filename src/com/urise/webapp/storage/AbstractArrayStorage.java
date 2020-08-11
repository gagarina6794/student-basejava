package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage extends AbstractStorage<Integer> {
    protected static final int STORAGE_LIMIT = 10_000;
    private Resume[] storage = new Resume[STORAGE_LIMIT];
    private int count = 0;

    public void clear() {
        Arrays.fill(storage, 0, count, null);
        count = 0;
    }

    protected Resume getFromStorage(Integer goalIndex) {
        return storage[ goalIndex];
    }

    protected void updateInStorage(Resume resume, Integer goalIndex) {
        storage[goalIndex] = resume;
    }

    protected void saveInStorage(Resume resume, Integer goalIndex) {
        if (count < storage.length) {
            saveResume(resume,goalIndex);
            count++;
        } else {
            throw new StorageException("Storage overflow", resume.getUuid());
        }
    }

    protected Resume[] getStorage(){
        return storage;
    }

    protected void deleteFromStorage(Integer goalIndex) {
        deleteResume(goalIndex);
        storage[count--] = null;
    }

    protected Resume[] getAll() {
       return Arrays.copyOfRange(storage, 0, count);
    }

    public int size(){
        return count;
    }

    @Override
    protected boolean isKeyExist(Integer checkKey) {
        return checkKey >= 0;
    }

    protected abstract void deleteResume(int index);

    protected abstract void saveResume(Resume resume, int index);
}
