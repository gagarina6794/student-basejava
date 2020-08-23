package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage<T> implements Storage {

    protected static final Comparator<Resume> NAME_COMPARATOR =
            Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid);

    @Override
    public void update(Resume resume) {
        T searchKey = getSearchKeyIfExist(resume.getUuid());
        doUpdate(resume, searchKey);
    }

    @Override
    public void save(Resume resume) {
        try {
            getSearchKeyIfExist(resume.getUuid());
        } catch (NotExistStorageException ex) {
            doSave(resume, findResumeKey(resume.getUuid()));
            return;
        }
        throw new ExistStorageException(resume.getUuid());
    }

    @Override
    public Resume get(String uuid) {
        T searchKey = getSearchKeyIfExist(uuid);
        return doGet(searchKey);
    }

    @Override
    public void delete(String uuid) {
        T searchKey = getSearchKeyIfExist(uuid);
        doDelete(searchKey);
    }

    private T getSearchKeyIfExist(String uuid) {
        T searchKey = findResumeKey(uuid);
        if (!isKeyExist(searchKey)){
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    public List<Resume> getAllSorted() {
        List<Resume> resumes = getAll();
        Collections.sort(resumes, NAME_COMPARATOR);
        return resumes;
    }

    protected abstract T findResumeKey(String uuid);

    protected abstract boolean isKeyExist(T checkKey);

    protected abstract void doUpdate(Resume resume, T searchKey);

    protected abstract void doSave(Resume resume, T searchKey);

    protected abstract Resume doGet(T searchKey);

    protected abstract void doDelete(T searchKey);

    protected abstract List<Resume> getAll();
}
