package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected int findResumeIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, count, searchKey);
    }

    @Override
    protected void deleteResume(int deleteIndex) {
        if (count - 1 - deleteIndex >= 0)
            System.arraycopy(storage, deleteIndex + 1, storage, deleteIndex, count - 1 - deleteIndex);
    }

    @Override
    protected void saveResume(Resume resume, int goalIndex) {
        goalIndex = -(goalIndex) - 1;
        System.arraycopy(storage, goalIndex, storage, goalIndex + 1, count - goalIndex);
        storage[goalIndex] = resume;
    }
}
