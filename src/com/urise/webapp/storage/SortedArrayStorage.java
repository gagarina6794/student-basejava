package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    public void update(Resume resume) {
        int goalIndex = findItemIndex(resume.getUuid());
        if (goalIndex > 0) {
            storage[goalIndex] = resume;
            SortArray();
        } else {
            System.out.println("Resume " + resume.getUuid() + " can't be found");
        }
    }

    @Override
    public void save(Resume resume) {
        int index = findItemIndex(resume.getUuid());
        if (index < 0) {
            if (count < storage.length) {
                storage[count++] = resume;
                SortArray();
               /* index = -(index) + 1;
                System.arraycopy(storage, index, storage, index + 1, count - index);
                count++;*/
            }
        } else {
            System.out.println("Resume " + resume.getUuid() + " exist in this storage");
        }

    }

    @Override
    protected int findItemIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, count, searchKey);
    }


    private void SortArray() {
        for (int i = 1; i < count; i++) {
            Resume goalItem = storage[i];
            int index = Arrays.binarySearch(storage, 0, count, goalItem);
            if (index < 0) {
                index = -(index) - 1;
            }
            System.arraycopy(storage, index, storage, index + 1, i - index);
            storage[index] = goalItem;
        }
    }
}
