package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.io.IOException;
import java.nio.file.Path;

public class ObjectPathStorage extends AbstractPathStorage {

    protected ObjectPathStorage(String directory){
        super(directory);
    }

    @Override
    protected void doWrite(Resume resume, Path path) throws IOException {
       // Files.write(path,resume);??????
    }

    @Override
    protected Resume doRead(Path path) throws IOException {
       // return (Resume)Files.readAllBytes(path); ??????????
        return null;
    }


}
