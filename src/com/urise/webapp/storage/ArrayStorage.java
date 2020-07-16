package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    protected int findItemIndex(String uuid) {
        int goalIndex = -1;
        for (int i = 0; i < count; i++) {
            if ((storage[i].getUuid()).equals(uuid)) {
                goalIndex = i;
                break;
            }
        }
        return goalIndex;
    }

    @Override
    protected void deleteItem(String uuid, int deleteIndex) {
        storage[deleteIndex] = storage[count];
        storage[count] = null;
    }


    @Override
    protected void saveItem(Resume resume) {
        storage[count++] = resume;
    }

}
