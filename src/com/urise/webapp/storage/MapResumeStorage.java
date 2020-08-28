package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

public class MapResumeStorage extends AbstractMapStorage<Resume> {

    @Override
    protected Resume findResumeKey(String uuid) {
        return storage.get(uuid);
    }

    @Override
    protected Resume doGet(Resume searchKey) {
        return searchKey;
    }

    @Override
    protected void doDelete(Resume searchKey) {
        storage.remove(searchKey.getUuid());
    }
}
