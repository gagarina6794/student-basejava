package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class PathStorage extends AbstractStorage<Path> {
    private Path directory;
    private StorageSerialization serialization;

    protected PathStorage(String dir, StorageSerialization serialization) {
        this.directory = Paths.get(dir);
        this.serialization = serialization;
        Objects.requireNonNull(directory, "directory must not be not");
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " is not directory");
        }

    }

    @Override
    protected Path findResumeKey(String uuid) {
        return directory.resolve(uuid);
    }

    @Override
    protected boolean isKeyExist(Path path) {
        return Files.exists(path);
    }

    @Override
    protected void doUpdate(Resume resume, Path path) {
        try {
            serialization.doWrite(resume, new ObjectOutputStream(Files.newOutputStream(path)));
        } catch (IOException e) {
            throw new StorageException("IO error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected void doSave(Resume resume, Path path) {
        try {
            Files.createFile(path);
            serialization.doWrite(resume, new ObjectOutputStream(Files.newOutputStream(path)));
        } catch (IOException e) {
            throw new StorageException("IO error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected Resume doGet(Path path) {
        try {
            return serialization.doRead(new ObjectInputStream(Files.newInputStream(path)));
        } catch (IOException e) {
            throw new StorageException("IO error", path.getFileName().toString(), e);
        }
    }


    @Override
    protected void doDelete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException("Path delete error", null);
        }
    }

    @Override
    protected Resume[] getAll() {
        try {
            Resume[] fileArray = new Resume[size()];
            int i = 0;
            for (var file : Files.list(directory).toArray()) {
                fileArray[i++] = doGet((Path) file);
            }
            return fileArray;
        } catch (IOException e) {
            throw new StorageException("Path getAll error", null);
        }
    }

    @Override
    public void clear() {
        try {
            Files.list(directory).forEach(this::doDelete);
        } catch (IOException e) {
            throw new StorageException("Path clear error", null);
        }
    }

    @Override
    public int size() {
        try {
            return (int) Files.list(directory).count();
        } catch (IOException e) {
            throw new StorageException("Path delete error", null);
        }

    }
}
