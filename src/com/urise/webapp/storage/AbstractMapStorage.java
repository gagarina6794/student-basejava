package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractMapStorage extends AbstractStorage {
    protected Map<String, Resume> storage = new HashMap<>();

    @Override
    protected Object findResumeKey(String uuid) {
        return storage.containsKey(uuid) ? uuid : null;
    }

    @Override
    protected void updateInStorage(Resume resume, Object searchKey) {
        storage.replace(resume.getUuid(), resume);
    }

    @Override
    protected boolean isKeyExist(Object checkKey) {
        return checkKey != null;
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
    protected List<Resume> storageAsSortedList() {
        Resume[] tempArray = storage.values().toArray(new Resume[size()]);
        Arrays.sort(tempArray, NAME_COMPARATOR);
        return Arrays.asList(tempArray);
    }

    @Override
    public int size() {
        return storage.size();
    }

}
