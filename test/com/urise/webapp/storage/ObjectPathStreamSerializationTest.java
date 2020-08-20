package com.urise.webapp.storage;

public class ObjectPathStreamSerializationTest extends AbstractStorageTest {

    public ObjectPathStreamSerializationTest() {
        super(new PathStorage("C:/Users/Lera/basejava/storage", new ObjectStreamSerialization()));
    }

}