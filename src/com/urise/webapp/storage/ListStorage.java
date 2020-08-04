package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListStorage extends AbstractStorage {
    private List<Resume> resumes = new ArrayList<>();

    @Override
    protected Object findResumeKey(String uuid) {
        return resumes.indexOf(new Resume(uuid, "noName"));
    }

    @Override
    protected boolean isKeyExist(Object checkKey) {
        return (int) checkKey >= 0;
    }

    @Override
    protected void updateInStorage(Resume resume, Object index) {
        resumes.set((int) index, resume);
    }

    @Override
    public void clear() {
        resumes.clear();
    }

    @Override
    protected void saveInStorage(Resume resume, Object index) {
        resumes.add(resume);
    }

    @Override
    protected Resume getFromStorage(Object index) {
        return resumes.get((int) index);
    }

    @Override
    protected void deleteFromStorage(Object index) {
        resumes.remove((int) index);
    }

    @Override
    protected List<Resume> storageAsSortedList() {
        List<Resume> tempList = new ArrayList<>(resumes);
        Collections.sort(tempList, NAME_COMPARATOR);
        return tempList;
    }

    @Override
    public int size() {
        return resumes.size();
    }


}