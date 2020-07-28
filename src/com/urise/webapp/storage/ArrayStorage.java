package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    protected Object findResumeKey(String uuid) {
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
    protected void deleteResume(int deleteIndex) {
        storage[deleteIndex] = storage[count-1];
    }

    @Override
    protected void saveResume(Resume resume, int goalIndex) {
        storage[count] = resume;
    }

}
