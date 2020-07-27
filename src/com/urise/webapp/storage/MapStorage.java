package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.HashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage {
    Map<String, Resume> storage = new HashMap<>();

    @Override
    protected int findResumeIndex(String uuid) {
        int goalIndex = 0;
        for (Map.Entry<String, Resume> entry : storage.entrySet()) {
            if (entry.getKey().equals(uuid))
                return goalIndex;
            goalIndex++;
        }
        return -1;
    }

    @Override
    protected void updateInStorage(Resume resume, int goalIndex) {
        storage.replace(resume.getUuid(), resume);
    }

    @Override
    protected void saveInStorage(Resume resume, int goalIndex) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    protected Resume getFromStorage(int goalIndex) {
        int currentIndex = 0;
        for (Map.Entry<String, Resume> entry : storage.entrySet()) {
            if (currentIndex == goalIndex)
                return entry.getValue();
            goalIndex++;
        }
        return null;
    }

    @Override
    protected void deleteFromStorage(int goalIndex) {
        String removeKey = "";
        int currentIndex = 0;
        for (Map.Entry<String, Resume> entry : storage.entrySet()) {
            if (currentIndex == goalIndex) {
                removeKey = entry.getKey();
                break;
            }
            goalIndex++;
        }
        storage.remove(removeKey);
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
