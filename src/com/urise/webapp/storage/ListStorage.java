package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.*;

public class ListStorage extends AbstractStorage {
    List<Resume> resumes = new ArrayList<>();

    @Override
    protected void updateInStorage(Resume resume) {
        resumes.set(resumes.indexOf(resume), resume);
    }

    @Override
    protected void clearStorage() {
        resumes.clear();
    }

    @Override
    protected void saveInStorage(Resume resume) {
        resumes.add(resume);
    }

    @Override
    protected Resume getFromStorage(String uuid) {
        return resumes.get(resumes.indexOf(new Resume(uuid)));
    }

    @Override
    protected void deleteFromStorage(String uuid) {
        resumes.remove(resumes.indexOf(new Resume(uuid)));
    }

    @Override
    protected Resume[] getAllStorage() {
        return resumes.stream().toArray(Resume[]::new);
    }
}
