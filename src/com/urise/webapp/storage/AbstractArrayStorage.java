package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage extends AbstractStorage {
    protected static final int STORAGE_LIMIT = 10_000;
    protected Resume[] storage = new Resume[STORAGE_LIMIT];

    protected void clearStorage() {
        Arrays.fill(storage, 0, count, null);
    }

    protected Resume getFromStorage(String uuid) {
        int goalIndex = findResumeIndex(uuid);
        if (goalIndex < 0) {
            throw new NotExistStorageException(uuid);
        }
        return storage[goalIndex];
    }

    protected void updateInStorage(Resume resume) {
        int goalIndex = findResumeIndex(resume.getUuid());
        if (goalIndex >= 0) {
            storage[goalIndex] = resume;
        } else {
            throw new NotExistStorageException(resume.getUuid());
        }
    }

    protected void saveInStorage(Resume resume) {
        int goalIndex = findResumeIndex(resume.getUuid());
        if (goalIndex < 0) {
            if (count < storage.length) {
                saveResume(resume, goalIndex);
            } else {
                throw new StorageException("Storage overflow", resume.getUuid());
            }
        } else {
            throw new ExistStorageException(resume.getUuid());
        }
    }

    protected void deleteFromStorage(String uuid) {
        int goalIndex = findResumeIndex(uuid);
        if (goalIndex >= 0) {
            deleteResume(uuid, goalIndex);
            storage[count] = null;
        } else {
            throw new NotExistStorageException(uuid);
        }
    }

    protected Resume[] getAllStorage() {
        return Arrays.copyOfRange(storage, 0, count);
    }

    protected abstract int findResumeIndex(String uuid);

    protected abstract void deleteResume(String uuid, int deleteIndex);

    protected abstract void saveResume(Resume resume, int saveIndex);
}
