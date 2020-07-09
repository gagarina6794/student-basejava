package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];
    private int count = 0;


    public void update(Resume resume) {
        int goalIndex = findItemIndex(resume.getUuid());
        if (goalIndex != -1) {
            storage[goalIndex] = resume;
        } else {
            System.out.println("Resume " + resume.getUuid() + " can't be found");
        }
    }

    public void clear() {
        Arrays.fill(storage, null);
        count = 0;
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

    public Resume get(String uuid) {
        for (int i = 0; i < count; i++) {
            if ((storage[i].getUuid()).equals(uuid)) {
                return storage[i];
            }
        }
        return null;
    }

    public void delete(String uuid) {
        int goalIndex = findItemIndex(uuid);
        if (goalIndex != -1) {

            for (int i = goalIndex; i < count - 1; i++) {
                storage[i] = storage[i + 1];
            }
            storage[count] = null;
            count--;
        } else {
            System.out.println("Resume " + uuid + " doesn't exist in this storage");
        }
    }

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

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, count);
    }

    public int size() {
        return count;
    }
}
