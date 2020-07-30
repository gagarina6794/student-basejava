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
        if (!isKeyExist(resume)){
            throw new NotExistStorageException(resume.getUuid());
        }
        return findResumeKey(resume.getUuid());
       /* Object goalIndex = findResumeKey(resume.getUuid());
        if (goalIndex.getClass() != String.class) {
            if ((int) goalIndex < 0) {

            }
        }
        return goalIndex;*/
    }

    protected abstract Object findResumeKey(String uuid);

    protected abstract boolean isKeyExist(Resume resume);

    protected abstract void updateInStorage(Resume resume, Object goalIndex);

    protected abstract void saveInStorage(Resume resume, Object goalIndex);

    protected abstract Resume getFromStorage(Object searchKey);

    protected abstract void deleteFromStorage(Object searchKey);
}
