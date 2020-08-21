package com.urise.webapp.storage;

import java.io.File;

public class ObjectFileStreamSerializationTest extends AbstractStorageTest {

    public ObjectFileStreamSerializationTest() {
        super(new FileStorage(new File(path), new ObjectStreamSerialization()));
    }

}