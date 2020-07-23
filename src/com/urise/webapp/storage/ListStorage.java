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
    protected void clearStorage() {
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
    protected Resume[] getAllStorage() {
        return resumes.stream().toArray(Resume[]::new);
    }

    @Override
    protected int getSize() {
        return resumes.size();
    }
}
