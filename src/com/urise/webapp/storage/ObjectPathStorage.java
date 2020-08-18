package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ObjectPathStorage extends AbstractPathStorage {

    protected ObjectPathStorage(String directory){
        super(directory);
    }

    @Override
    protected void doWrite(Resume resume, Path path) throws IOException {
        Files.write(path,resume);
    }

    @Override
    protected Resume doRead(Path path) throws IOException {
        return (Resume)Files.readAllBytes(path);
    }


}
