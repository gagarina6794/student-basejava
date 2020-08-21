package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractMapStorage<T> extends AbstractStorage<T> {
    Map<String, Resume> storage = new HashMap<>();

    @Override
    protected void doUpdate(Resume resume, T searchKey) {
        storage.replace(resume.getUuid(), resume);
    }

    @Override
    protected void doSave(Resume resume, T searchKey) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    protected boolean isKeyExist(T checkKey) {
        return checkKey != null;
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    protected Resume[] getAll() {
        return storage.values().toArray(new Resume[size()]);
    }

    @Override
    public int size() {
        return storage.size();
    }

}
