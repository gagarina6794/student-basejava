package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage<Integer> {
    private List<Resume> resumes = new ArrayList<>();

    @Override
    protected Integer findResumeKey(String uuid) {
        int goalIndex = 0;
        for(Resume item: resumes){
            if (item.getUuid() == uuid)
                return goalIndex;
            goalIndex++;
        }
        return -1;
    }

    @Override
    protected boolean isKeyExist(Integer checkKey) {
        return checkKey >= 0;
    }

    @Override
    protected void doUpdate(Resume resume, Integer index) {
        resumes.set( index, resume);
    }

    @Override
    public void clear() {
        resumes.clear();
    }

    @Override
    protected void doSave(Resume resume, Integer index) {
        resumes.add(resume);
    }

    @Override
    protected Resume doGet(Integer index) {
        return resumes.get(index);
    }

    @Override
    protected void doDelete(Integer index) {
        resumes.remove(index.intValue());
    }

    @Override
    protected Resume[] getAll() {
        return  resumes.toArray(new Resume[resumes.size()]);
    }

    @Override
    public int size() {
        return resumes.size();
    }


}
