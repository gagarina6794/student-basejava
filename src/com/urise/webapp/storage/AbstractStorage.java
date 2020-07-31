package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {

    @Override
    public void update(Resume resume) {
        Object searchKey = getSearchKeyIfExist(resume);
        updateInStorage(resume, searchKey);
    }

    @Override
    public void save(Resume resume) {
        try {
            getSearchKeyIfExist(resume);
        } catch (NotExistStorageException ex) {
            saveInStorage(resume, findResumeKey(resume.getUuid()));
            return;
        }
        throw new ExistStorageException(resume.getUuid());
    }

    @Override
    public Resume get(String uuid) {
        Object searchKey = getSearchKeyIfExist(new Resume(uuid));
        return getFromStorage(searchKey);
    }

    @Override
    public void delete(String uuid) {
        Object searchKey = getSearchKeyIfExist(new Resume(uuid));
        deleteFromStorage(searchKey);
    }

    private Object getSearchKeyIfExist(Resume resume) {
        Object goalIndex = findResumeKey(resume.getUuid());
        if (!isKeyExist(goalIndex)){
            throw new NotExistStorageException(resume.getUuid());
        }
        return goalIndex;
    }

    protected abstract Object findResumeKey(String uuid);

    protected abstract boolean isKeyExist(Object checkKey);

    protected abstract void updateInStorage(Resume resume, Object goalIndex);

    protected abstract void saveInStorage(Resume resume, Object goalIndex);

    protected abstract Resume getFromStorage(Object searchKey);

    protected abstract void deleteFromStorage(Object searchKey);
}
