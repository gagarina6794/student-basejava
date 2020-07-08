/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    int count = 0;

    void clear() {
        for (int i = 0; i < count; i++) {
            storage[i] = null;
        }
        count = 0;
    }

    void save(Resume r) {
        if (count < storage.length) {
            storage[count++] = r;
        }
    }

    Resume get(String uuid) {
        for (int i = 0; i < count; i++) {
            if ((storage[i].uuid).equals(uuid)) {
                return storage[i];
            }
        }
        return null;
    }

    void delete(String uuid) {
        if (get(uuid) == null){
            return;
        }
        int goalIndex = -1;
        for (int i = 0; i < count; i++) {
            if ((storage[i].uuid).equals(uuid)) {
                goalIndex = i;
                break;
            }
        }

        for (int i = goalIndex; i < count - 1; i++) {
            storage[i] = storage[i + 1];
        }
        if (goalIndex == 0) {
            storage[count] = null;
            if (count != 0) {
                count--;
            }
        } else {
            storage[--count] = null;
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        Resume[] resumes = new Resume[count];
        for (int i = 0; i < count; i++) {
            resumes[i] = storage[i];
        }
        return resumes;
    }

    int size() {
        return count;
    }
}
