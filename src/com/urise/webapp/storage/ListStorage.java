package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.*;

public class ListStorage extends AbstractStorage {
    List<Resume> resumes = new ArrayList<>();

    @Override
    protected Object findResumeKey(String uuid) {
        return resumes.indexOf(new Resume(uuid));
    }

    @Override
    protected void updateInStorage(Resume resume, Object goalIndex) {
        resumes.set((int)goalIndex, resume);
    }

    @Override
    public void clear() {
        resumes.clear();
    }

    @Override
    protected void saveInStorage(Resume resume, Object goalIndex) {
        resumes.add(resume);
    }

    @Override
    protected Resume getFromStorage(Object goalIndex) {
        return resumes.get((int)goalIndex);
    }

    @Override
    protected void deleteFromStorage(Object goalIndex) {
        resumes.remove((int)goalIndex);
    }

    @Override
    public Resume[] getAll() {
        return resumes.stream().toArray(Resume[]::new);
    }

    @Override
    public int size() {
        return resumes.size();
    }
}
