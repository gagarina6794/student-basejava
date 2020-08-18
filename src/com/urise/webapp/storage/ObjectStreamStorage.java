package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.io.*;

public class ObjectStreamStorage extends AbstractFileStorage {

    protected ObjectStreamStorage(File directory){
        super(directory);
    }

    @Override
    protected void doWrite(Resume resume, OutputStream os) throws IOException {
        try(ObjectOutputStream oos = new ObjectOutputStream(os)){
            oos.writeObject(resume);
        }
    }

    @Override
    protected Resume doRead(InputStream is) throws IOException {
        try(ObjectInputStream iis = new ObjectInputStream(is)) {
            return (Resume) iis.readObject();
        }catch (ClassNotFoundException e){
            throw  new StorageException("Error read resume", null, e);
        }
    }


}
