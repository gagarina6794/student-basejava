package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected Object findResumeKey(String uuid) {
        return Arrays.binarySearch(getStorage(), 0, size(), new Resume(uuid,""));
    }

    @Override
    protected void deleteResume(int index) {
        Resume[] resumes = getStorage();
        if (size() - 1 - index >= 0)
            System.arraycopy(resumes, index + 1, resumes, index, size() - 1 - index);
    }

    @Override
    protected void saveResume(Resume resume, int index) {
        index = -(index) - 1;
        Resume[] resumes = getStorage();
        System.arraycopy(resumes, index, resumes, index + 1, size() - index);
        resumes[index] = resume;
    }
}
