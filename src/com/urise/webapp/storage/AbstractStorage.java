package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {
    protected int count = 0;

    @Override
    public void update(Resume resume) {
        updateInStorage(resume);
    }

    @Override
    public void clear() {
        clearStorage();
        count = 0;
    }

    @Override
    public void save(Resume resume) {
        saveInStorage(resume);
        count++;

    }

    @Override
    public Resume get(String uuid) {
       return getFromStorage(uuid);
    }

    @Override
    public void delete(String uuid) {
        deleteFromStorage(uuid);
        count--;
    }

    @Override
    public Resume[] getAll() {
        return getAllStorage();
    }

    @Override
    public int size() {
        return count;
    }

    protected abstract void updateInStorage(Resume resume);

    protected abstract void clearStorage();

    protected abstract void saveInStorage(Resume resume);

    protected abstract Resume getFromStorage(String uuid);

    protected abstract void deleteFromStorage(String uuid);

    protected abstract Resume[] getAllStorage();

}
