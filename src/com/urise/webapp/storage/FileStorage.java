package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.serializer.StorageSerialization;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileStorage extends AbstractStorage<File> {
    private File directory;
    private StorageSerialization serialization;

    protected FileStorage(File directory, StorageSerialization serialization) {
        Objects.requireNonNull(directory, "directory must not be not");
        this.serialization = serialization;
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
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
    protected void doUpdate(Resume resume, File file) {
        try {
            serialization.doWrite(resume, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected void doSave(Resume resume, File file) {
        try {
            file.createNewFile();
            doUpdate(resume, file);
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected Resume doGet(File file) {
        try {
            return serialization.doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected void doDelete(File file) {
        if (!file.delete())
            throw new NotExistStorageException("This file doesn't exist: " + file.getName());
    }

    @Override
    protected List<Resume> getAll() {
        List<Resume> fileList = new ArrayList<>();
        File[] files = directoryAsList();
        for (File dir : directory.listFiles()) {
            fileList.add(doGet(dir));
        }
        return fileList;
    }

    @Override
    public void clear() {
        File[] files = directoryAsList();
        for (File dir : files) {
            dir.delete();
        }
    }

    @Override
    public int size() {
        return directoryAsList().length;
    }

    File[] directoryAsList() {
        File[] files = directory.listFiles();
        if (files == null) {
            throw new StorageException("This directory doesn't exist");
        }
        return files;
    }
}
