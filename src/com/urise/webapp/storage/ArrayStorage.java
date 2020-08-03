package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    protected Object findResumeKey(String uuid) {
        Resume[] resumes = getStorage();
        int goalIndex = -1;
        for (int i = 0; i < size(); i++) {
            if ((resumes[i].getUuid()).equals(uuid)) {
                goalIndex = i;
                break;
            }
        }
        return goalIndex;
    }

    @Override
    protected void deleteResume(int index) {
        Resume[] resumes = getStorage();
        resumes[index] = resumes[size()-1];
    }

    @Override
    protected void saveResume(Resume resume, int index) {
        Resume[] resumes = getStorage();
        resumes[size()] = resume;
    }

}
