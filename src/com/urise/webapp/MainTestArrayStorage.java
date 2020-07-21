package com.urise.webapp;

import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.ListStorage;
import com.urise.webapp.storage.Storage;

/**
 * Test for your com.urise.webapp.storage.ArrayStorage implementation
 */
public class MainTestArrayStorage {
    private static final Storage ARRAY_STORAGE = new ListStorage();

    public static void main(String[] args) {
        Resume r1 = new Resume();

        r1.setUuid("uuid2");
        Resume r2 = new Resume();
        r2.setUuid("uuid3");
        Resume r3 = new Resume();
        r3.setUuid("uuid1");

        ARRAY_STORAGE.save(r1);
        ARRAY_STORAGE.save(r2);
        ARRAY_STORAGE.save(r3);

        Resume r4 = new Resume();
        Resume r5 = new Resume();

        try {
            r4.setUuid("uuid2");
            // r5.setUuid("uiu2");

            ARRAY_STORAGE.update(r4);
            ARRAY_STORAGE.update(r5);
        } catch (Exception ex) {

        }

        System.out.println("Get r1: " + ARRAY_STORAGE.get(r1.getUuid()));

        System.out.println("Size: " + ARRAY_STORAGE.size());
        try {
            System.out.println("Get dummy: " + ARRAY_STORAGE.get("dummy"));
        }
        catch (Exception e) {
        }


        printAll();
        ARRAY_STORAGE.delete(r1.getUuid());
        System.out.println("--------------\n---------------\n----------");
        printAll();

        ARRAY_STORAGE.clear();
        printAll();

        System.out.println("Size: " + ARRAY_STORAGE.size());
    }

    static void printAll() {
        System.out.println("\nGet All");
        for (Resume r : ARRAY_STORAGE.getAll()) {
            System.out.println(r);
        }
    }
}
