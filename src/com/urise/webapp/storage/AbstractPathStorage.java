package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public abstract class AbstractPathStorage extends AbstractStorage<Path> {
    private Path directory;

    protected AbstractPathStorage(String dir){
        this.directory = Paths.get(dir);
        Objects.requireNonNull(directory,"directory must not be not");
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)){
            throw new IllegalArgumentException(dir + " is not directory");
        }

    }

    @Override
    protected Path findResumeKey(String uuid) {
        return  ;
    }

    @Override
    protected boolean isKeyExist(Path path) {
        return Files.exists(path);
    }

    @Override
    protected void updateInStorage(Resume resume, Path path) {
        try {
            doWrite(resume,path);
        } catch (IOException e) {
            throw new StorageException("IO error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected void saveInStorage(Resume resume, Path path) {
        try {
            Files.createFile(path);
            doWrite(resume,path);
        } catch (IOException e) {
            throw new StorageException("IO error", path.getFileName().toString(), e);
        }
    }

    protected abstract void doWrite(Resume resume, Path os) throws IOException;

    @Override
    protected Resume getFromStorage(Path path) {
        try {
            return doRead(path);
        } catch (IOException e) {
            throw new StorageException("IO error", path.getFileName().toString(), e);
        }
    }

    protected abstract Resume doRead(Path path) throws IOException;


    @Override
    protected void deleteFromStorage(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException("Path delete error",null);
        }
    }

    @Override
    protected Resume[] getAll() {
        try {
            return (Resume[]) Files.list(directory).toArray();
        } catch (IOException e) {
            throw new StorageException("Path getAll error",null);
        }
    }

    @Override
    public void clear() {
        try {
            Files.list(directory).forEach(this::deleteFromStorage);
        } catch (IOException e) {
            throw new StorageException("Path clear error",null);
        }
    }

    @Override
    public int size() {
        try {
            return (int) Files.list(directory).count();
        } catch (IOException e) {
            throw new StorageException("Path delete error",null);
        }

    }
}
