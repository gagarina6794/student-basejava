package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.io.*;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {
    private File directory;

    protected AbstractFileStorage(File directory){
        Objects.requireNonNull(directory,"directory must not be not");
        if (!directory.isDirectory()){
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()){
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
        this.directory = directory;
    }

    @Override
    protected File findResumeKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected boolean isKeyExist(File file) {
        return file.exists();
    }

    @Override
    protected void updateInStorage(Resume resume, File file) {
        try {
            doWrite(resume,new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected void saveInStorage(Resume resume, File file) {
        try {
            file.createNewFile();
            ///////
            doWrite(resume,new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    protected abstract void doWrite(Resume resume, OutputStream os) throws IOException;

    @Override
    protected Resume getFromStorage(File file) {
        try {
            return doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    protected abstract Resume doRead(InputStream is) throws IOException;


    @Override
    protected void deleteFromStorage(File file) {
        file.delete();
    }

    @Override
    protected Resume[] getAll() {
        Resume[] fileArray = new Resume[size()];
        File[] files = directory.listFiles();
        int i = 0;
        if (files != null) {
            for (File dir : files) {
                fileArray[i++] = getFromStorage(dir);
            }
        }
        return fileArray;
    }

    @Override
    public void clear() {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File dir : files) {
                dir.delete();
            }
        }
    }

    @Override
    public int size() {
        return directory.listFiles() != null?directory.listFiles().length:0;
    }
}
