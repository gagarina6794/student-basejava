package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

public class MapStorage extends AbstractMapStorage {

    @Override
    protected Object findResumeKey(String uuid) {
        return storage.containsKey(uuid) ? uuid : null;
    }

    @Override
    protected Resume getFromStorage(Object searchKey) {
        return storage.get(searchKey);
    }

    @Override
    protected void deleteFromStorage(Object searchKey) {
        storage.remove(searchKey);
    }

}
