package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    public void update(Resume resume) {
        int goalIndex = findItemIndex(resume.getUuid());
        if (goalIndex != -1) {
            storage[goalIndex] = resume;
        } else {
            System.out.println("Resume " + resume.getUuid() + " can't be found");
        }
    }

    public void save(Resume resume) {
        if (findItemIndex(resume.getUuid()) == -1) {
            if (count < storage.length) {
                storage[count++] = resume;
            }
        } else {
            System.out.println("Resume " + resume.getUuid() + " exist in this storage");
        }
    }

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

}
