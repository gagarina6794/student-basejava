package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {

    @Override
    public void update(Resume resume) {
        int goalIndex = findResumeIndex(resume.getUuid());
        if (goalIndex >= 0) {
            updateInStorage(resume, goalIndex);
        } else {
            throw new NotExistStorageException(resume.getUuid());
        }
    }

    @Override
    public void save(Resume resume) {
        int goalIndex = findResumeIndex(resume.getUuid());
        if (goalIndex < 0) {
            saveInStorage(resume, goalIndex);
        } else {
            throw new ExistStorageException(resume.getUuid());
        }
    }

    @Override
    public Resume get(String uuid) {
        int goalIndex = findResumeIndex(uuid);
        if (goalIndex < 0) {
            throw new NotExistStorageException(uuid);
        }
        return getFromStorage(goalIndex);
    }

    @Override
    public void delete(String uuid) {
        int goalIndex = findResumeIndex(uuid);
        if (goalIndex >= 0) {
            deleteFromStorage(goalIndex);
        } else {
            throw new NotExistStorageException(uuid);
        }
    }

    protected abstract int findResumeIndex(String uuid);

    protected abstract void updateInStorage(Resume resume, int goalIndex);

    protected abstract void saveInStorage(Resume resume, int goalIndex);

    protected abstract Resume getFromStorage(int goalIndex);

    protected abstract void deleteFromStorage(int goalIndex);
}
