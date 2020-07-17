package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage {
    protected static final int STORAGE_LIMIT = 10_000;
    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int count = 0;

   /* public int getStorageLimit(){
        return STORAGE_LIMIT;
    };*/

    public int size() {
        return count;
    }

    public void clear() {
        Arrays.fill(storage, 0, count, null);
        count = 0;
    }

    public Resume get(String uuid) {
        int goalIndex = findItemIndex(uuid);
        if (goalIndex < 0) {
            throw new NotExistStorageException(uuid);
        }
        return storage[goalIndex];
    }

    public void update(Resume resume) {
        int goalIndex = findItemIndex(resume.getUuid());
        if (goalIndex >= 0) {
            storage[goalIndex] = resume;
        } else {
            throw new NotExistStorageException(resume.getUuid());
        }
    }

    public void save(Resume resume) {
        int goalIndex = findItemIndex(resume.getUuid());
        if (goalIndex < 0) {
            if (count < storage.length) {
                saveResume(resume, goalIndex);
                count++;
            } else {
                throw new StorageException("Storage overflow", resume.getUuid());
            }
        } else {
            throw new ExistStorageException(resume.getUuid());
        }
    }

    public void delete(String uuid) {
        int goalIndex = findItemIndex(uuid);
        if (goalIndex >= 0) {
            deleteResume(uuid, goalIndex);
            storage[count--] = null;
        } else {
            throw new NotExistStorageException(uuid);
        }
    }

    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, count);
    }

    protected abstract int findItemIndex(String uuid);

    protected abstract void deleteResume(String uuid, int deleteIndex);

    protected abstract void saveResume(Resume resume, int saveIndex);
}
