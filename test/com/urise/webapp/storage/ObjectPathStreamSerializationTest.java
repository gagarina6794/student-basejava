package com.urise.webapp.storage;

public class ObjectPathStreamSerializationTest extends AbstractStorageTest {

    public ObjectPathStreamSerializationTest() {
        super(new PathStorage(path, new ObjectStreamSerialization()));
    }

}