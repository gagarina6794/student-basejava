package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

public class MapResumeStorage extends MapStorage {

    @Override
    protected Object findResumeKey(String uuid) {
        return storage.containsKey(uuid)? storage.get(uuid):null;
    }

    @Override
    protected void updateInStorage(Resume resume, Object searchKey) {
        storage.replace(resume.getUuid(), resume);
    }

    @Override
    protected void saveInStorage(Resume resume, Object searchKey) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    protected Resume getFromStorage(Object searchKey) {
        return storage.get(((Resume)searchKey).getUuid());
    }

    @Override
    protected void deleteFromStorage(Object searchKey) {
        storage.remove(((Resume)searchKey).getUuid());
    }

}
