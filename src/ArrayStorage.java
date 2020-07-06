/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    int nextElemIndex = 0;

    void clear() {
        for (int i = 0; i < nextElemIndex; i++) {
            storage[i] = null;
        }
    }

    void save(Resume r) {
        if (nextElemIndex < 10000)
            storage[nextElemIndex++] = r;
    }

    Resume get(String uuid) throws Exception {

        for (int i = 0; i < nextElemIndex; i++) {
            if ((storage[i].uuid).equals(uuid)) {
                return storage[i];
            }
        }
        throw new Exception("this item doesn't exist");
    }

    void delete(String uuid) {

        int goalIndex = 0;
        for (int i = 0; i < nextElemIndex; i++) {
            if ((storage[i].uuid).equals(uuid)) {
                goalIndex = i;
                break;
            }
        }

        for (int i = goalIndex; i < nextElemIndex - 1; i++) {
            storage[i] = storage[i + 1];
        }
        if (goalIndex == 0) storage[nextElemIndex] = null;
        else

            storage[--nextElemIndex] = null;
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return storage;
    }

    int size() {
        return nextElemIndex;
    }
}
