package com.urise.webapp.storage;

import com.urise.webapp.storage.serializer.ObjectStreamSerialization;

public class ObjectPathStreamSerializationTest extends AbstractStorageTest {

    public ObjectPathStreamSerializationTest() {
        super(new PathStorage(STORAGE, new ObjectStreamSerialization()));
    }

}