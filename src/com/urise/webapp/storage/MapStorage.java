package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.HashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage {
    private Map<String, Resume> storage = new HashMap<>();

    @Override
    protected Object findResumeKey(String uuid) {
        return storage.containsKey(uuid)? uuid:null;
    }

    @Override
    protected void updateInStorage(Resume resume, Object searchKey) {
        storage.replace(resume.getUuid(), resume);
    }
    @Override
    protected boolean isKeyExist(Resume resume) {
        return findResumeKey(resume.getUuid()) != null;
    }

    @Override
    protected void saveInStorage(Resume resume, Object searchKey) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    protected Resume getFromStorage(Object searchKey) {
        return storage.get(searchKey);
    }

    @Override
    protected void deleteFromStorage(Object searchKey) {
        storage.remove(searchKey);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        return  storage.values().toArray(new Resume[size()]);
    }

    @Override
    public int size() {
        return storage.size();
    }
}
