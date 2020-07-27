package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.HashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage {
    Map<String, Resume> storage = new HashMap<>();

    @Override
    protected Object findResumeKey(String uuid) {
        return storage.containsKey(uuid)? uuid:-1;
    }

    @Override
    protected void updateInStorage(Resume resume, Object searchKey) {
        storage.replace(resume.getUuid(), resume);
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
        Resume[] resumesArray = new Resume[storage.size()];
        int i = 0;
        for (Map.Entry<String, Resume> entry : storage.entrySet()) {
            resumesArray[i++] = entry.getValue();
        }
        return resumesArray;
    }

    @Override
    public int size() {
        return storage.size();
    }
}
