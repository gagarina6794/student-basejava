package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];
    private int count = 0;

    private int findItemIndex(String uuid) {
        int goalIndex = -1;
        for (int i = 0; i < count; i++) {
            if ((storage[i].getUuid()).equals(uuid)) {
                goalIndex = i;
                break;
            }
        }
        return goalIndex;
    }

    public void update(Resume r) {
        if (findItemIndex(r.getUuid()) != -1) {
            storage[findItemIndex(r.getUuid())] = r;
        }
        else {
            System.out.println("Resume can't be found");
        }
    }

    public void clear() {
        for (int i = 0; i < count; i++) {
            storage[i] = null;
        }
        count = 0;
    }

    public void save(Resume r) {
        if (findItemIndex(r.getUuid()) == -1) {
            if (count < storage.length) {
                storage[count++] = r;
            }
        } else {
            System.out.println("Resume exist in this storage");
        }
    }

    public Resume get(String uuid) {
        for (int i = 0; i < count; i++) {
            if ((storage[i].getUuid()).equals(uuid)) {
                return storage[i];
            }
        }
        return null;
    }

    public void delete(String uuid) {
        if (findItemIndex(uuid) != -1) {

            for (int i = findItemIndex(uuid); i < count - 1; i++) {
                storage[i] = storage[i + 1];
            }
            storage[count] = null;
            count--;
        } else {
            System.out.println("Resume doesn't exist in this storage");
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        Resume[] resumes = new Resume[count];
        for (int i = 0; i < count; i++) {
            resumes[i] = storage[i];
        }
        return resumes;
    }

    public int size() {
        return count;
    }
}
