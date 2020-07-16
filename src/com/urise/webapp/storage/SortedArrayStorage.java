package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {


    @Override
    protected int findItemIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, count, searchKey);
    }

    @Override
    protected void deleteItem(String uuid, int deleteIndex) {
        if (count - 1 - deleteIndex >= 0)
            System.arraycopy(storage, deleteIndex + 1, storage, deleteIndex, count - 1 - deleteIndex);
        storage[count] = null;

    }


    @Override
    protected void saveItem(Resume resume) {
        storage[count++] = resume;
        for (int i = 1; i < count; i++) {
            Resume goalItem = storage[i];
            int goalIndex = Arrays.binarySearch(storage, 0, count, goalItem);
            if (goalIndex < 0) {
                goalIndex = -(goalIndex) - 1;
            }
            System.arraycopy(storage, goalIndex, storage, goalIndex + 1, i - goalIndex);
            storage[goalIndex] = goalItem;
        }
    }
}
