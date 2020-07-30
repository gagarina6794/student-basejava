package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage extends AbstractStorage {
    protected static final int STORAGE_LIMIT = 10_000;
    private Resume[] storage = new Resume[STORAGE_LIMIT];
    private int count = 0;

    public void clear() {
        Arrays.fill(storage, 0, count, null);
        count = 0;
    }

    protected Resume getFromStorage(Object goalIndex) {
        return storage[(int) goalIndex];
    }

    protected void updateInStorage(Resume resume, Object goalIndex) {
        storage[(int) goalIndex] = resume;
    }

    protected void saveInStorage(Resume resume, Object goalIndex) {
        if (count < storage.length) {
            saveResume(resume,(int) goalIndex);
            count++;
        } else {
            throw new StorageException("Storage overflow", resume.getUuid());
        }
    }

    protected Resume[] getStorage(){
        return storage;
    }

    protected void deleteFromStorage(Object goalIndex) {
        deleteResume((int) goalIndex);
        storage[count--] = null;
    }

    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, count);
    }

    public int size(){
        return count;
    }

    @Override
    protected boolean isKeyExist(Resume resume) {
        return (int) findResumeKey(resume.getUuid()) >= 0;
    }

    protected abstract void deleteResume(int index);

    protected abstract void saveResume(Resume resume, int index);
}
