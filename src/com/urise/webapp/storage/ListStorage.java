package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.*;

public class ListStorage extends AbstractStorage {
    List<Resume> resumes = new ArrayList<>();

    @Override
    protected int findResumeIndex(String uuid) {
        return resumes.indexOf(new Resume(uuid));
    }

    @Override
    protected void updateInStorage(Resume resume, int goalIndex) {
        resumes.set(goalIndex, resume);
    }

    @Override
    public void clear() {
        resumes.clear();
    }

    @Override
    protected void saveInStorage(Resume resume, int goalIndex) {
        resumes.add(resume);
    }

    @Override
    protected Resume getFromStorage(int goalIndex) {
        return resumes.get(goalIndex);
    }

    @Override
    protected void deleteFromStorage(int goalIndex) {
        resumes.remove(goalIndex);
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
