package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.serializer.StorageSerialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathStorage extends AbstractStorage<Path> {
    private Path directory;
    private StorageSerialization serialization;

    protected PathStorage(String dir, StorageSerialization serialization) {
        Objects.requireNonNull(dir, "directory must not be not");
        this.directory = Paths.get(dir);
        this.serialization = serialization;
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
            doUpdate(resume, path);
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
            throw new StorageException("Path delete error");
        }
    }

    @Override
    protected List<Resume> getAll() {
        return directoryToList().map(this::doGet).collect(Collectors.toList());
    }

    @Override
    public void clear() {
        directoryToList().forEach(this::doDelete);
    }

    @Override
    public int size() {
        return (int) directoryToList().count();
    }

    private Stream<Path> directoryToList() {
        try {
            return Files.list(directory);
        } catch (IOException e) {
            throw new StorageException("Directory error");
        }
    }
}
