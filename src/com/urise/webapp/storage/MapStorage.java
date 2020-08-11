package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

public class MapStorage extends AbstractMapStorage<String> {

    @Override
    protected String findResumeKey(String uuid) {
        return storage.containsKey(uuid) ? uuid : null;
    }

    @Override
    protected Resume getFromStorage(String searchKey) {
        return storage.get(searchKey);
    }

    @Override
    protected void deleteFromStorage(String searchKey) {
        storage.remove(searchKey);
    }

}
