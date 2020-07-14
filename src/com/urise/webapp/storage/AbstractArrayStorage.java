package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage {
    protected static final int STORAGE_LIMIT = 10_000;
    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int count = 0;

    public int size()
    {
        return count;
    }

    public void clear() {
        Arrays.fill(storage,0,count, null);
        count = 0;
    }

    public Resume get(String uuid) {
        int index = findItemIndex(uuid);
        if (index == -1) {
            System.out.println("Resume " + uuid + " doesn't exist");
            return null;
        }
        return storage[index];
    }

    public void delete(String uuid) {
        int goalIndex = findItemIndex(uuid);
        if (goalIndex != -1) {
            if (count - 1 - goalIndex >= 0)
                System.arraycopy(storage, goalIndex + 1, storage, goalIndex, count - 1 - goalIndex);
            storage[count] = null;
            count--;
        } else {
            System.out.println("Resume " + uuid + " doesn't exist in this storage");
        }
    }

    public Resume[] getAll() {
        return Arrays.copyOfRange(storage,0, count);
    }

    protected abstract int findItemIndex(String uuid);
}
