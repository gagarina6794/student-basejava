package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

public class MapResumeStorage extends AbstractMapStorage {

    @Override
    protected Object findResumeKey(String uuid) {
        return storage.get(uuid);
    }

    @Override
    protected Resume getFromStorage(Object searchKey) {
        return (Resume) searchKey;
    }

    @Override
    protected void deleteFromStorage(Object searchKey) {
        storage.remove(((Resume) searchKey).getUuid());
    }

}
